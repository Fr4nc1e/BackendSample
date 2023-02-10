package com.sample.routes

import com.sample.data.models.NotificationMessage
import com.sample.data.requests.notification.PostNotificationRequest
import com.sample.routes.util.userId
import com.sample.service.FollowService
import com.sample.service.NotificationService
import com.sample.util.Constants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.sendPostNotification(
    notificationService: NotificationService,
    followService: FollowService
) {
    authenticate {
        get("/api/post/notification") {
            val title = call.parameters["title"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val description = call.parameters["description"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val targetUserIds = followService.getFollowers(
                ownUserId = call.userId,
                userId = call.userId,
            ).map { it.userId }

            notificationService.sendPostNotification(
                PostNotificationRequest(
                    includeExternalUserIds = targetUserIds,
                    contents = NotificationMessage(en = description),
                    headings = NotificationMessage(en = title),
                    appId = Constants.ONESIGNAL_APP_ID
                ).also { println(it) }
            ).also {
                if (it) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
        }
    }
}
