package tech.bnuuy.anigiri.feature.player.presentation

import tech.bnuuy.anigiri.feature.player.api.data.model.Comment
import tech.bnuuy.anigiri.feature.player.api.data.model.Episode

data class PlayerState(
    val episode: Episode? = null,
    val isLoading: Boolean = true,
    val error: Throwable? = null,

    val comments: List<Comment>? = null,
    val areCommentsLoading: Boolean = false,
    val commentsError: Throwable? = null,
) {
    val isReadyToPlay: Boolean
        get() = episode != null && !isLoading && error == null
}