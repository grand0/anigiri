package tech.bnuuy.anigiri.feature.player.presentation

sealed interface PlayerAction {
    data object LoadEpisode : PlayerAction
    data object LoadComments : PlayerAction
    data class SendComment(val comment: String) : PlayerAction
}