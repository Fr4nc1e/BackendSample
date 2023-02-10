package com.sample.service

import com.sample.data.repository.notification.OneSignalRepository
import com.sample.data.requests.notification.PostNotificationRequest

class NotificationService(
    private val repository: OneSignalRepository
) {
    suspend fun sendPostNotification(
        notification: PostNotificationRequest,
    ): Boolean {
        return repository.sendPostNotification(notification)
    }
}