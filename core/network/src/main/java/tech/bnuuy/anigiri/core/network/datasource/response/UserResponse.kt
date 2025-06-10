package tech.bnuuy.anigiri.core.network.datasource.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName(ID_KEY)
    val id: Int,
    @SerialName(LOGIN_KEY)
    val login: String?,
    @SerialName(NICKNAME_KEY)
    val nickname: String?,
    @SerialName(EMAIL_KEY)
    val email: String?,
    @SerialName(AVATAR_PATH_KEY)
    val avatarUrl: String?,
) {
    companion object {
        const val ID_KEY = "id"
        const val LOGIN_KEY = "login"
        const val NICKNAME_KEY = "nickname"
        const val EMAIL_KEY = "email"
        const val AVATAR_PATH_KEY = "avatar_path"
    }
}
