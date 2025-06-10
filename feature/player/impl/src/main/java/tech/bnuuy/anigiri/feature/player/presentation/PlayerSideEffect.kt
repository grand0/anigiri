package tech.bnuuy.anigiri.feature.player.presentation

sealed interface PlayerSideEffect {
    data object SendCommentSuccess : PlayerSideEffect
    data class SendCommentError(val error: Throwable?) : PlayerSideEffect
}
