package tech.bnuuy.anigiri.feature.profile.api.data.model

import kotlinx.datetime.Instant

data class Profile(
    val id: Int,
    val login: String?,
    val email: String?,
    val avatarUrl: String?,
    val nickname: String?,
    val isBanned: Boolean,
    val createdAt: Instant,
)
