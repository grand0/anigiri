package tech.bnuuy.anigiri.core.network.datasource.response

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentResponse(
    @SerialName("id")
    val id: String,
    @SerialName("text")
    val text: String,
    @SerialName("episode_id")
    val episodeId: String,
    @SerialName("user")
    val user: UserResponse,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("modified_at")
    val modifiedAt: Instant,
)
