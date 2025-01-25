package tech.bnuuy.anigiri.feature.home.presentation.model

internal sealed interface HomeAction {
    data object Refresh : HomeAction
    data object FetchRandomRelease : HomeAction
}
