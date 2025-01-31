package tech.bnuuy.anigiri.feature.search.presentation

sealed interface SearchSideEffect {
    data class ShowError(val error: Throwable?) : SearchSideEffect
}
