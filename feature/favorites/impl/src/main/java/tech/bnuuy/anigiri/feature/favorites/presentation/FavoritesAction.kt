package tech.bnuuy.anigiri.feature.favorites.presentation

sealed interface FavoritesAction {
    data object Refresh : FavoritesAction
}
