package tech.bnuuy.anigiri.core.network.datasource.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    @SerialName("login")
    val login: String,
    @SerialName("password")
    val password: String,
)
