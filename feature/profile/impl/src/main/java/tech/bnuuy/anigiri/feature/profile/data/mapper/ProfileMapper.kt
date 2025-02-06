package tech.bnuuy.anigiri.feature.profile.data.mapper

import kotlinx.datetime.Instant
import tech.bnuuy.anigiri.core.network.datasource.response.ProfileResponse
import tech.bnuuy.anigiri.core.network.util.buildStorageUrl
import tech.bnuuy.anigiri.feature.profile.api.data.model.Profile

internal fun ProfileResponse.toDomain(): Profile {
    val avatarUrl = avatar.preview?.let { buildStorageUrl(it) }
    
    return Profile(
        id = id,
        login = login,
        email = email,
        avatarUrl = avatarUrl,
        nickname = nickname,
        isBanned = isBanned,
        createdAt = Instant.parse(createdAt),
    )
}
