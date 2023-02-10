package com.sample.routes

import com.google.gson.Gson
import com.sample.data.requests.user.UpdateProfileRequest
import com.sample.data.responses.BasicApiResponse
import com.sample.data.responses.user.UserResponseItem
import com.sample.routes.util.userId
import com.sample.service.UserService
import com.sample.util.ApiResponseMessages
import com.sample.util.Constants.BANNER_IMAGE_PATH
import com.sample.util.Constants.BASE_URL
import com.sample.util.Constants.PROFILE_PICTURE_PATH
import com.sample.util.QueryParams
import com.sample.util.save
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File

fun Route.searchUser(
    userService: UserService
) {
    authenticate {
        get("/api/user/search") {
            val query = call.parameters[QueryParams.PARAM_QUERY]
            if (query.isNullOrBlank()) {
                call.respond(
                    HttpStatusCode.OK,
                    listOf<UserResponseItem>()
                )
                return@get
            }

            val searchResults = userService.searchUser(query, call.userId)

            call.respond(
                HttpStatusCode.OK,
                searchResults
            )
        }
    }
}

fun Route.getUserProfile(
    userService: UserService
) {
    authenticate {
        get("/api/user/profile") {
            val userId = call.parameters[QueryParams.PARAM_USER_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val profileResponse = userService.getUserProfile(
                userId = userId,
                callerUserId = call.userId
            )

            if (profileResponse != null) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true,
                        data = profileResponse
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
                return@get
            }
        }
    }
}

fun Route.updateUser(
    userService: UserService
) {
    val gson: Gson by inject()
    authenticate {
        put("/api/user/update") {
            val multipart = call.receiveMultipart()
            var updateProfileRequest: UpdateProfileRequest? = null
            var profilePicFileName: String? = null
            var bannerImageFileName: String? = null
            multipart.forEachPart { partData ->
                when (partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "update_profile_data") {
                            updateProfileRequest = gson.fromJson(
                                partData.value,
                                UpdateProfileRequest::class.java
                            )
                        }
                    }

                    is PartData.FileItem -> {
                        if (partData.name == "profile_picture") {
                            profilePicFileName = partData.save(PROFILE_PICTURE_PATH)
                            File(
                                "$PROFILE_PICTURE_PATH${
                                    userService.getUserById(call.userId)
                                        ?.profileImageUrl
                                        ?.takeLastWhile {
                                            it != '/'
                                        }
                                }"
                            ).delete()
                        } else if (partData.name == "banner_image") {
                            bannerImageFileName = partData.save(BANNER_IMAGE_PATH)
                            File(
                                "$BANNER_IMAGE_PATH${
                                    userService.getUserById(call.userId)
                                        ?.bannerUrl
                                        ?.takeLastWhile {
                                            it != '/'
                                        }
                                }"
                            ).delete()
                        }
                    }

                    else -> Unit
                }
            }

            val profilePictureUrl = "${BASE_URL}profile_pictures/$profilePicFileName"
            val bannerImageUrl = "${BASE_URL}banner_images/$bannerImageFileName"

            updateProfileRequest?.let { request ->
                val updateAcknowledged = userService.updateUser(
                    userId = call.userId,
                    profileImageUrl = if (profilePicFileName == null) {
                        null
                    } else profilePictureUrl,
                    bannerUrl = if (bannerImageFileName == null) {
                        null
                    } else bannerImageUrl,
                    updateProfileRequest = request
                )
                if (updateAcknowledged) {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true
                        )
                    )
                } else {
                    File("$PROFILE_PICTURE_PATH/$profilePicFileName").delete()
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
        }
    }
}
