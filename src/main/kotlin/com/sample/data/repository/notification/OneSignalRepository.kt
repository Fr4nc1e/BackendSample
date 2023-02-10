package com.sample.data.repository.notification

import com.sample.data.requests.notification.PostNotificationRequest

interface OneSignalRepository {
    suspend fun sendPostNotification(notification: PostNotificationRequest): Boolean
}