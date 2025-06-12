package tech.bnuuy.anigiri.feature.collections.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import tech.bnuuy.anigiri.core.network.datasource.enumeration.CollectionType
import tech.bnuuy.anigiri.feature.collections.api.data.model.PagedContent
import tech.bnuuy.anigiri.feature.collections.api.data.model.Release
import tech.bnuuy.anigiri.feature.collections.api.usecase.GetCollectionReleasesUseCase
import tech.bnuuy.anigiri.feature.collections.paging.CollectionPagingSource

class CollectionsViewModel(
    private val getCollectionReleasesUseCase: GetCollectionReleasesUseCase,
) : ContainerHost<CollectionsState, Nothing>, ViewModel() {
    override val container = container<CollectionsState, Nothing>(CollectionsState())

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingDataFlow = container.stateFlow
        .map { it.collectionType }
        .distinctUntilChanged()
        .flatMapLatest { collectionType ->
            Pager(PagingConfig(pageSize = 15, enablePlaceholders = false)) {
                CollectionPagingSource(
                    collectionType,
                    getCollectionReleasesUseCase,
                    onPageReceived = { onPageReceived(it) }
                )
            }.flow
        }.cachedIn(viewModelScope)

    fun dispatch(action: CollectionsAction) {
        when (action) {
            is CollectionsAction.FetchCollection -> fetchCollection(action.type)
        }
    }

    private fun fetchCollection(type: CollectionType) = intent {
        reduce { state.copy(collectionType = type) }
    }

    private fun onPageReceived(page: PagedContent<Release>) = intent {
        reduce { state.copy(totalItems = page.totalItems) }
    }
}
