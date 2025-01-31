package tech.bnuuy.anigiri.feature.search.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import tech.bnuuy.anigiri.feature.search.api.data.model.CatalogSearchFilter
import tech.bnuuy.anigiri.feature.search.api.usecase.SearchCatalogUseCase
import tech.bnuuy.anigiri.feature.search.paging.SearchPagingSource

internal class SearchViewModel(
    private val searchCatalogUseCase: SearchCatalogUseCase,
) : ContainerHost<SearchState, SearchSideEffect>, ViewModel() {
    override val container = container<SearchState, SearchSideEffect>(SearchState())
    
    private val currentQuery = MutableStateFlow("")
    
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val pagingDataFlow = currentQuery
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            Pager(PagingConfig(pageSize = 15, enablePlaceholders = false)) {
                SearchPagingSource(searchCatalogUseCase, CatalogSearchFilter(search = query))
            }.flow
        }.cachedIn(viewModelScope)

    fun dispatch(action: SearchAction) {
        when (action) {
            is SearchAction.Search -> search(action.query)
        }
    }

    fun search(query: String) = intent {
        reduce { 
            state.copy(query = query)
        }
        currentQuery.value = query
    }
}
