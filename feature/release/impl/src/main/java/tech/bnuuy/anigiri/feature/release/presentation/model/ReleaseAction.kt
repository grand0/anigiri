package tech.bnuuy.anigiri.feature.release.presentation.model

sealed interface ReleaseAction {
    data object Refresh : ReleaseAction
    data object AddToFavorites : ReleaseAction
    data object RemoveFromFavorites : ReleaseAction
}
