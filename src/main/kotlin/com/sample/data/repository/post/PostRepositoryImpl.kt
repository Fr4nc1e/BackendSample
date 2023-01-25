package com.sample.data.repository.post

import com.sample.data.models.Following
import com.sample.data.models.Like
import com.sample.data.models.Post
import com.sample.data.models.User
import com.sample.data.responses.PostResponse
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase

class PostRepositoryImpl(
    db: CoroutineDatabase
) : PostRepository {

    private val users = db.getCollection<User>()
    private val posts = db.getCollection<Post>()
    private val likes = db.getCollection<Like>()
    private val following = db.getCollection<Following>()

    override suspend fun createPost(post: Post): Boolean {
        return posts.insertOne(post).wasAcknowledged().also { wasAcknowledged ->
            if (wasAcknowledged) {
                users.updateOneById(
                    post.userId,
                    inc(User::postCount, 1)
                )
            }
        }
    }

    override suspend fun deletePost(postId: String) {
        posts.findOneById(postId)?.also {
            users.updateOneById(
                it.userId,
                inc(User::postCount, -1)
            )
        }
        posts.deleteOneById(postId)
    }

    override suspend fun getPostByFollows(
        ownUserId: String,
        page: Int,
        pageSize: Int
    ): List<PostResponse> {
        val userIdsFromFollows = following.find(
            Following::followingUserId eq ownUserId
        )
            .toList()
            .map {
                it.followedUserId
            }

        return posts.find(
            or(
                Post::userId `in` userIdsFromFollows,
                Post::userId eq ownUserId
            )
        )
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Post::timestamp)
            .toList()
            .map { post ->
                val isLiked = likes.findOne(
                    and(
                    Like::parentId eq post.id,
                    Like::userId eq ownUserId
                                )
                ) != null
                val user = users.findOneById(post.userId) ?: return emptyList()
                post.toPostResponse(
                    username = user.username,
                    profilePictureUrl = user.profileImageUrl,
                    isLiked = isLiked,
                    isOwnPost = ownUserId == post.userId
                )
            }
    }

    override suspend fun getPost(postId: String): Post? {
        return posts.findOneById(postId)
    }

    override suspend fun getPostForLike(
        ownUserId: String,
        userId: String,
        page: Int,
        pageSize: Int
    ): List<PostResponse> {
        val parentId = likes.find(
            Like::userId eq userId
        )
            .toList()
            .map { it.parentId }

        return posts.find(
            Post::id `in` parentId
        )
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Post::timestamp)
            .toList()
            .map { post ->
                val user = users.findOneById(post.userId) ?: return emptyList()
                val isLiked = likes.findOne(
                    and(
                    Like::parentId eq post.id,
                    Like::userId eq ownUserId
                )
                ) != null
                post.toPostResponse(
                    username = user.username,
                    profilePictureUrl = user.profileImageUrl,
                    isLiked = isLiked,
                    isOwnPost = ownUserId == post.userId
                )
            }
    }

    override suspend fun getPostForProfile(
        ownUserId: String,
        userId: String,
        page: Int,
        pageSize: Int
    ): List<PostResponse> {
        val user = users.findOneById(userId) ?: return emptyList()
        return posts.find(Post::userId eq userId)
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Post::timestamp)
            .toList()
            .map { post ->
                val isLiked = likes.findOne(
                    and(
                    Like::parentId eq post.id,
                    Like::userId eq ownUserId
                )
                ) != null
                post.toPostResponse(
                    username = user.username,
                    profilePictureUrl = user.profileImageUrl,
                    isLiked = isLiked,
                    isOwnPost = ownUserId == post.userId
                )
            }
    }

    override suspend fun getPostDetails(
        ownUserId: String,
        postId: String
    ): PostResponse? {
        val isLiked = likes.findOne(
            and(
                Like::parentId eq postId,
                Like::userId eq ownUserId
            )
        ) != null
        val post =  posts.findOneById(postId) ?: return null
        val user = users.findOneById(post.userId) ?: return null
        return post.toPostResponse(
            username = user.username,
            profilePictureUrl = user.profileImageUrl,
            isLiked = isLiked,
            isOwnPost = ownUserId == post.userId
        )
    }
}
