package com.sample.routes.util

import com.sample.plugins.email
import com.sample.util.ApiResponseMessages
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.ifEmailBelongsToUser(
    userId: String,
    validateEmail: suspend (
        email: String,
        userId: String
    ) -> Boolean,
    onSuccess: suspend () -> Unit
) {
    val isEmailByUser = validateEmail(
        call.principal<JWTPrincipal>()?.email ?: "",
        userId
    )
    if (isEmailByUser) {
        onSuccess()
    } else {
        call.respond(
            HttpStatusCode.Unauthorized,
            ApiResponseMessages.UNAUTHORIZED
        )
    }
}
