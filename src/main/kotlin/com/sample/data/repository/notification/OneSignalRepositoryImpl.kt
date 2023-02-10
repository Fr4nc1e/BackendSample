package com.sample.data.repository.notification

import com.sample.data.requests.notification.PostNotificationRequest
import com.sample.util.Constants.ONESIGNAL_API_KEY
import com.sample.util.Constants.ONESIGNAL_NOTIFICATION_URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class OneSignalRepositoryImpl(
    private val httpClient: HttpClient
) : OneSignalRepository {
    override suspend fun sendPostNotification(
        notification: PostNotificationRequest
    ): Boolean {
        return try {
            httpClient.post(ONESIGNAL_NOTIFICATION_URL) {
                header("Authorization", "Basic $ONESIGNAL_API_KEY")
                contentType(ContentType.Application.Json)
                setBody(notification)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}