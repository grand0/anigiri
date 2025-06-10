package tech.bnuuy.anigiri.feature.player.presentation

sealed interface PlayerAction {
    data object Load : PlayerAction
    data object LoadComments : PlayerAction
    data class SendComment(val comment: String) : PlayerAction
}
