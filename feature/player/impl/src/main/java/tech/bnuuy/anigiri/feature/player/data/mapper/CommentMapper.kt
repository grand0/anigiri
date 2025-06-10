package tech.bnuuy.anigiri.feature.player.data.mapper

import tech.bnuuy.anigiri.core.network.datasource.response.CommentResponse
import tech.bnuuy.anigiri.feature.player.api.data.model.Comment

internal fun List<CommentResponse>.mapList(): List<Comment> = map {
    it.toDomain()
}

internal fun CommentResponse.toDomain(): Comment {
    val userDomain = user.toDomain()

    return Comment(
        id = id,
        text = text,
        episodeId = episodeId,
        user = userDomain,
        createdAt = createdAt,
        modifiedAt = modifiedAt,
    )
}
