package tech.bnuuy.anigiri.feature.collections.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import tech.bnuuy.anigiri.core.network.datasource.enumeration.CollectionType
import tech.bnuuy.anigiri.feature.collections.api.data.model.PagedContent
import tech.bnuuy.anigiri.feature.collections.api.data.model.Release
import tech.bnuuy.anigiri.feature.collections.api.usecase.GetCollectionReleasesUseCase
import tech.bnuuy.anigiri.feature.collections.data.model.CollectionFilter

internal class CollectionPagingSource(
    private val collectionType: CollectionType,
    private val getCollectionReleasesUseCase: GetCollectionReleasesUseCase,
    private val onPageReceived: (PagedContent<Release>) -> Unit,
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
            val resp = getCollectionReleasesUseCase(CollectionFilter(
                page = page,
                collectionType = collectionType,
            ))
            onPageReceived(resp)
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
