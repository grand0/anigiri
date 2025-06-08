package tech.bnuuy.anigiri.core.network.datasource.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("login")
    val login: String?,
    @SerialName("nickname")
    val nickname: String?,
    @SerialName("email")
    val email: String?,
    @SerialName("avatar_path")
    val avatarUrl: String?,
)
