package tech.bnuuy.anigiri.feature.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import tech.bnuuy.anigiri.feature.search.api.data.model.PagedContent
import tech.bnuuy.anigiri.feature.search.api.data.model.Release
import tech.bnuuy.anigiri.feature.search.api.usecase.FetchGenresUseCase
import tech.bnuuy.anigiri.feature.search.api.usecase.FetchYearsUseCase
import tech.bnuuy.anigiri.feature.search.api.usecase.SearchCatalogUseCase
import tech.bnuuy.anigiri.feature.search.data.model.CatalogSearchUiFilter
import tech.bnuuy.anigiri.feature.search.paging.SearchPagingSource

internal class SearchViewModel(
    private val searchCatalogUseCase: SearchCatalogUseCase,
    private val fetchGenresUseCase: FetchGenresUseCase,
    private val fetchYearsUseCase: FetchYearsUseCase,
) : ContainerHost<SearchState, SearchSideEffect>, ViewModel() {
    override val container = container<SearchState, SearchSideEffect>(SearchState())
    
    init {
        loadFilters()
    }
    
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val pagingDataFlow = container.stateFlow
        .map { it.filter }
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { filter ->
            Pager(PagingConfig(pageSize = 15, enablePlaceholders = false)) {
                SearchPagingSource(
                    searchCatalogUseCase,
                    filter,
                    onPageReceived = { onPageReceived(it) },
                )
            }.flow
        }.cachedIn(viewModelScope)

    fun dispatch(action: SearchAction) {
        when (action) {
            is SearchAction.Search -> search(action.filter)
        }
    }

    private fun search(filter: CatalogSearchUiFilter) = intent {
        reduce { 
            state.copy(filter = filter)
        }
    }
    
    private fun loadFilters() = intent {
        reduce { state.copy(filtersLoading = true) }
        coroutineScope {
            launch { loadGenres() }
            launch { loadYears() }
        }
        reduce { state.copy(filtersLoading = false) }
    }
    
    @OptIn(OrbitExperimental::class)
    private suspend fun loadGenres() = subIntent {
        runCatching {
            fetchGenresUseCase()
        }.onSuccess { genres ->
            reduce { state.copy(genres = genres) }
        }
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun loadYears() = subIntent {
        runCatching {
            fetchYearsUseCase()
        }.onSuccess { years ->
            reduce { state.copy(
                minYear = years.minOrNull(),
                maxYear = years.maxOrNull(),
            ) }
        }
    }
    
    private fun onPageReceived(page: PagedContent<Release>) = intent {
        reduce { state.copy(totalItems = page.totalItems) }
    }
}
