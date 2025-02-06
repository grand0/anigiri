package tech.bnuuy.anigiri.feature.search.presentation

import tech.bnuuy.anigiri.feature.search.data.model.CatalogSearchUiFilter

sealed interface SearchAction {
    data class Search(val filter: CatalogSearchUiFilter) : SearchAction
    data object ClearSearchHistory : SearchAction
}
