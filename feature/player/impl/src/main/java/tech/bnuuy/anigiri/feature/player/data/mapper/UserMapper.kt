package tech.bnuuy.anigiri.feature.player.data.mapper

import tech.bnuuy.anigiri.core.network.datasource.response.UserResponse
import tech.bnuuy.anigiri.core.network.util.buildStorageUrl
import tech.bnuuy.anigiri.feature.player.api.data.model.User

internal fun UserResponse.toDomain(): User {
    val srcUrl = avatarUrl?.let { buildStorageUrl(it) }

    return User(
        id = id,
        nickname = nickname ?: login ?: id.toString(),
        avatarUrl = srcUrl,
    )
}
