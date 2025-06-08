package tech.bnuuy.anigiri.core.network.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import tech.bnuuy.anigiri.core.network.datasource.response.CommentResponse
import tech.bnuuy.anigiri.core.network.datasource.response.UserResponse
import tech.bnuuy.anigiri.core.network.util.getInstant

class CommentsDataSource(
    private val db: FirebaseFirestore,
) {

    // TODO: paginate queries
    suspend fun getCommentsForEpisode(episodeId: String): List<CommentResponse> =
        withContext(Dispatchers.IO) {
            val result = db.collection("comments")
                .whereEqualTo("episode_id", episodeId)
                .orderBy("created_at", Query.Direction.DESCENDING)
                .get()
                .await()
            result
                .filter { it.contains("text") && it.contains("user") }
                .map { doc ->
                    val userId = doc.getDocumentReference("user")!!
                    val user = userId.get().await().run {
                        UserResponse(
                            id = getLong("id")?.toInt() ?: 0,
                            login = getString("login"),
                            nickname = getString("nickname"),
                            email = getString("email"),
                            avatarUrl = getString("avatar_path"),
                        )
                    }
                    val createdAt = doc.getInstant("created_at") ?: Instant.DISTANT_PAST
                    val modifiedAt = doc.getInstant("modified_at") ?: createdAt
                    CommentResponse(
                        id = doc.id,
                        text = doc.getString("text") ?: "",
                        episodeId = doc.getString("episode_id") ?: "",
                        user = user,
                        createdAt = createdAt,
                        modifiedAt = modifiedAt,
                    )
                }
        }
}
