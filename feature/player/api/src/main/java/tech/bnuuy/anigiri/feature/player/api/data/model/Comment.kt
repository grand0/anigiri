package tech.bnuuy.anigiri.feature.player.api.data.model

import kotlinx.datetime.Instant

data class Comment(
    val id: String,
    val text: String,
    val episodeId: String,
    val user: User,
    val createdAt: Instant,
    val modifiedAt: Instant,
)
