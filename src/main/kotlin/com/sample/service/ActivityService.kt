package com.sample.service

import com.sample.data.models.Activity
import com.sample.data.repository.activity.ActivityRepository
import com.sample.data.repository.comment.CommentRepository
import com.sample.data.repository.post.PostRepository
import com.sample.data.responses.ActivityResponse
import com.sample.data.util.ActivityType
import com.sample.data.util.ParentType
import com.sample.util.Constants

class ActivityService(
    private val activityRepository: ActivityRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository
) {

    suspend fun createActivity(
        activity: Activity
    ) {
        activityRepository.createActivity(activity)
    }

    suspend fun deleteActivity(
        activityId: String
    ) : Boolean {
        return activityRepository.deleteActivity(activityId)
    }

    suspend fun getActivitiesForUser(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_Activity_PAGE_SIZE
    ) : List<ActivityResponse> {
        return activityRepository.getActivitiesForUser(
            userId = userId,
            page = page,
            pageSize = pageSize
        )
    }

    suspend fun addCommentActivity(
        byUserId: String,
        postId: String
    ) : Boolean {
        val userIdOfPost = postRepository.getPost(postId)?.userId ?: return false

        activityRepository.createActivity(
            Activity(
                timestamp = System.currentTimeMillis(),
                byUserId = byUserId,
                toUserId = userIdOfPost,
                type = ActivityType.CommentedOnPost.type,
                parentId = postId
            )
        )

        return true
    }

    suspend fun addLikeActivity(
        byUserId: String,
        parentType: ParentType,
        parentId: String
    ) : Boolean {
        val toUserId = when (parentType) {
            is ParentType.Post -> {
                postRepository.getPost(parentId)?.userId
            }
            is ParentType.Comment -> {
                commentRepository.getComment(parentId)?.userId
            }
            is ParentType.None -> {
                return false
            }
        } ?: return false

        if (toUserId == byUserId) return false

        activityRepository.createActivity(
            Activity(
                timestamp = System.currentTimeMillis(),
                byUserId = byUserId,
                toUserId = toUserId,
                type = when (parentType) {
                    is ParentType.Post -> ActivityType.LikedPost.type
                    is ParentType.Comment -> ActivityType.LikedComment.type
                    else -> ActivityType.LikedPost.type
                },
                parentId = parentId
            )
        )

        return true
    }
}