package tech.bnuuy.anigiri.feature.search.presentation

import tech.bnuuy.anigiri.feature.search.data.model.CatalogSearchFilter

sealed interface SearchAction {
    data class Search(val filter: CatalogSearchFilter) : SearchAction
    data object ClearSearchHistory : SearchAction
}
