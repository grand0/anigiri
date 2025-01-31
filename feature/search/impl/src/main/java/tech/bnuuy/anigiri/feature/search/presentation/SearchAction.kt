package tech.bnuuy.anigiri.feature.search.presentation

sealed interface SearchAction {
    data class Search(val query: String) : SearchAction
}
