package com.sample.data.repository.activity

import com.sample.data.models.Activity
import com.sample.data.responses.ActivityResponse
import com.sample.util.Constants

interface ActivityRepository {

    suspend fun getActivitiesForUser(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_Activity_PAGE_SIZE
    ): List<ActivityResponse>

    suspend fun createActivity(activity: Activity)

    suspend fun deleteActivity(activityId: String): Boolean
}