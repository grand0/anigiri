package tech.bnuuy.anigiri.feature.search.presentation

import tech.bnuuy.anigiri.feature.search.api.data.model.Genre
import tech.bnuuy.anigiri.feature.search.api.data.model.SearchQuery
import tech.bnuuy.anigiri.feature.search.data.model.CatalogSearchUiFilter

data class SearchState(
    val filter: CatalogSearchUiFilter = CatalogSearchUiFilter(),
    val filtersLoading: Boolean = true,
    val genres: List<Genre> = emptyList(),
    val minYear: Int? = null,
    val maxYear: Int? = null,
    val totalItems: Int = 0,
    val searchHistory: List<SearchQuery> = emptyList(),
)
