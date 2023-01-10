package com.sample.data.repository.post

import com.sample.data.models.Following
import com.sample.data.models.Post
import com.sample.data.models.User
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.`in`

class PostRepositoryImpl(
    db: CoroutineDatabase
) : PostRepository {

    private val users = db.getCollection<User>()
    private val posts = db.getCollection<Post>()
    private val following = db.getCollection<Following>()

    override suspend fun createPostIfUserExists(post: Post): Boolean {
        val doesUserExist = users
            .findOneById(post.userId) != null

        if (!doesUserExist) {
            return false
        }

        posts.insertOne(post)
        return true
    }

    override suspend fun deletePost(postId: String) {
        posts.deleteOneById(postId)
    }

    override suspend fun getPostByFollows(
        userId: String,
        page: Int,
        pageSize: Int
    ): List<Post> {
        val userIdsFromFollows = following.find(
            Following::followingUserId eq userId
        )
            .toList()
            .map {
                it.followedUserId
            }

        return posts.find(
            Post::userId `in` userIdsFromFollows
        )
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Post::timestamp)
            .toList()
    }

    override suspend fun getPost(postId: String): Post? {
        return posts.findOneById(postId)
    }
}
