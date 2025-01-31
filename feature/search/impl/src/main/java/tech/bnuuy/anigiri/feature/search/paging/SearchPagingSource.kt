package tech.bnuuy.anigiri.feature.search.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import tech.bnuuy.anigiri.feature.search.api.data.model.CatalogSearchFilter
import tech.bnuuy.anigiri.feature.search.api.data.model.Release
import tech.bnuuy.anigiri.feature.search.api.usecase.SearchCatalogUseCase

class SearchPagingSource(
    private val searchCatalogUseCase: SearchCatalogUseCase,
    private val filter: CatalogSearchFilter,
) : PagingSource<Int, Release>() {
    
    override fun getRefreshKey(state: PagingState<Int, Release>): Int? {
        return state.anchorPosition?.let { 
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Release> {
        try {
            val page = params.key ?: 1
            val resp = searchCatalogUseCase(filter.copy(
                page = page,
            ))
            return LoadResult.Page(
                data = resp.data,
                prevKey = if (page <= 1) null else page - 1,
                nextKey = if (page >= resp.totalPages) null else page + 1,
            )
        } catch (e: Throwable) {
            return LoadResult.Error(e)
        }
    }
}
