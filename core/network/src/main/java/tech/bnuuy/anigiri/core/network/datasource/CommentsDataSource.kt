package tech.bnuuy.anigiri.core.network.datasource

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import tech.bnuuy.anigiri.core.network.datasource.response.CommentResponse
import tech.bnuuy.anigiri.core.network.datasource.response.UserResponse
import tech.bnuuy.anigiri.core.network.exception.NotAuthorizedException
import tech.bnuuy.anigiri.core.network.session.AppSession
import tech.bnuuy.anigiri.core.network.util.getInstant

class CommentsDataSource(
    private val db: FirebaseFirestore,
    private val appSession: AppSession,
) {

    // TODO: paginate queries
    suspend fun getCommentsForEpisode(episodeId: String): List<CommentResponse> =
        withContext(Dispatchers.IO) {
            val result = db.collection(COMMENTS_COLLECTION_KEY)
                .whereEqualTo(CommentResponse.EPISODE_ID_KEY, episodeId)
                .orderBy(CommentResponse.CREATED_AT_KEY, Query.Direction.DESCENDING)
                .get()
                .await()
            result
                .filter { it.contains(CommentResponse.TEXT_KEY) && it.contains(CommentResponse.USER_KEY) }
                .map { doc ->
                    val userRef = doc.getDocumentReference(CommentResponse.USER_KEY)!!
                    val user = userRef.get().await().run {
                        UserResponse(
                            id = userRef.id.toIntOrNull() ?: 0,
                            login = getString(UserResponse.LOGIN_KEY),
                            nickname = getString(UserResponse.NICKNAME_KEY),
                            email = getString(UserResponse.EMAIL_KEY),
                            avatarUrl = getString(UserResponse.AVATAR_PATH_KEY),
                        )
                    }
                    val createdAt = doc.getInstant(CommentResponse.CREATED_AT_KEY) ?: Instant.DISTANT_PAST
                    val modifiedAt = doc.getInstant(CommentResponse.MODIFIED_AT_KEY) ?: createdAt
                    CommentResponse(
                        id = doc.id,
                        text = doc.getString(CommentResponse.TEXT_KEY) ?: "",
                        episodeId = doc.getString(CommentResponse.EPISODE_ID_KEY) ?: "",
                        user = user,
                        createdAt = createdAt,
                        modifiedAt = modifiedAt,
                    )
                }
        }

    suspend fun sendCommentForEpisode(
        text: String,
        episodeId: String,
    ): Unit = withContext(Dispatchers.IO) {
        val user = appSession.getAuthorizedUser()
        if (user == null) {
            throw NotAuthorizedException()
        }

        val userData = hashMapOf<String, Any?>(
            UserResponse.LOGIN_KEY to user.login,
            UserResponse.NICKNAME_KEY to user.nickname,
            UserResponse.EMAIL_KEY to user.email,
            UserResponse.AVATAR_PATH_KEY to user.avatar.preview,
        )
        val userRef = db.collection(USERS_COLLECTION_KEY).document(user.id.toString())
        userRef.set(userData)

        val createdAt = Timestamp.now()
        val commentData = hashMapOf<String, Any>(
            CommentResponse.TEXT_KEY to text,
            CommentResponse.EPISODE_ID_KEY to episodeId,
            CommentResponse.USER_KEY to userRef,
            CommentResponse.CREATED_AT_KEY to createdAt,
            CommentResponse.MODIFIED_AT_KEY to createdAt,
        )

        db.collection(COMMENTS_COLLECTION_KEY).add(commentData)
    }

    companion object {
        const val COMMENTS_COLLECTION_KEY = "comments"
        const val USERS_COLLECTION_KEY = "users"
    }
}
