package tech.bnuuy.anigiri.feature.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import tech.bnuuy.anigiri.feature.favorites.api.data.model.PagedContent
import tech.bnuuy.anigiri.feature.favorites.api.data.model.Release
import tech.bnuuy.anigiri.feature.favorites.api.usecase.FetchFavoriteReleasesUseCase
import tech.bnuuy.anigiri.feature.favorites.paging.FavoritesPagingSource

internal class FavoritesViewModel(
    private val fetchFavoriteReleasesUseCase: FetchFavoriteReleasesUseCase,
) : ContainerHost<FavoritesState, Nothing>, ViewModel() {
    override val container = container<FavoritesState, Nothing>(FavoritesState())
    
    val pagingDataFlow = Pager(PagingConfig(pageSize = 15, enablePlaceholders = false)) {
        FavoritesPagingSource(
            fetchFavoriteReleasesUseCase,
            onPageReceived = { onPageReceived(it) },
        )
    }.flow.cachedIn(viewModelScope)
    
    private fun onPageReceived(page: PagedContent<Release>) = intent {
        reduce { state.copy(totalItems = page.totalItems) }
    }
}
