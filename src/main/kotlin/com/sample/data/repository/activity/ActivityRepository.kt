package com.sample.data.repository.activity

import com.sample.data.models.Activity
import com.sample.util.Constants

interface ActivityRepository {

    suspend fun getActivityiesForUser(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_Activity_PAGE_SIZE
    ): List<Activity>

    suspend fun createActivity(activity: Activity)

    suspend fun deleteActivity(activityId: String): Boolean
}