package com.sample.data.repository.activity

import com.sample.data.models.Activity
import com.sample.data.models.User
import com.sample.data.responses.ActivityResponse
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class ActivityRepositoryImpl(
    db: CoroutineDatabase
): ActivityRepository {
    private val users = db.getCollection<User>()
    private val activities = db.getCollection<Activity>()

    override suspend fun getActivitiesForUser(
        userId: String,
        page: Int,
        pageSize: Int
    ): List<ActivityResponse> {
        val activities = activities.find(Activity::toUserId eq userId)
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Activity::timestamp)
            .toList()
        return activities.mapIndexed { i, activity ->
            ActivityResponse(
                timestamp = activity.timestamp,
                userId = activity.byUserId,
                parentId = activity.parentId,
                type = activity.type,
                username = users.findOneById(activity.byUserId)?.username ?: "",
                profileImageUrl = users.findOneById(activity.byUserId)?.profileImageUrl ?: "",
                id = activity.id
            )
        }
    }

    override suspend fun createActivity(activity: Activity) {
        activities.insertOne(activity)
    }

    override suspend fun deleteActivity(activityId: String): Boolean {
        return activities.deleteOne(activityId).wasAcknowledged()
    }
}