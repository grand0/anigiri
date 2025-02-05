package tech.bnuuy.anigiri.core.network.datasource.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("login")
    val login: String?,
    @SerialName("email")
    val email: String?,
    @SerialName("avatar")
    val avatar: ProfileAvatarResponse,
    @SerialName("nickname")
    val nickname: String?,
    @SerialName("is_banned")
    val isBanned: Boolean,
    @SerialName("created_at")
    val createdAt: String,
) {
    
    @Serializable
    data class ProfileAvatarResponse(
        @SerialName("preview")
        val preview: String?,
        @SerialName("thumbnail")
        val thumb: String?,
    )
}
