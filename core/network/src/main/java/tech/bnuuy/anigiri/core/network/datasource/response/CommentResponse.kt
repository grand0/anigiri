package tech.bnuuy.anigiri.core.network.datasource.response

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentResponse(
    @SerialName(ID_KEY)
    val id: String,
    @SerialName(TEXT_KEY)
    val text: String,
    @SerialName(EPISODE_ID_KEY)
    val episodeId: String,
    @SerialName(USER_KEY)
    val user: UserResponse,
    @SerialName(CREATED_AT_KEY)
    val createdAt: Instant,
    @SerialName(MODIFIED_AT_KEY)
    val modifiedAt: Instant,
) {
    companion object {
        const val ID_KEY = "id"
        const val TEXT_KEY = "text"
        const val EPISODE_ID_KEY = "episode_id"
        const val USER_KEY = "user"
        const val CREATED_AT_KEY = "created_at"
        const val MODIFIED_AT_KEY = "modified_at"
    }
}
