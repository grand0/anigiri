package tech.bnuuy.anigiri.feature.collections.data.mapper

import tech.bnuuy.anigiri.core.network.datasource.response.MetaContentResponse
import tech.bnuuy.anigiri.feature.collections.api.data.model.PagedContent

fun <T, R> MetaContentResponse<T>.toPagedContent(
    mapper: (T) -> R,
) : PagedContent<R> {
    return with(meta.pagination) {
        PagedContent(
            data = data.map(mapper),
            totalItems = total,
            itemsCount = count,
            itemsPerPage = perPage,
            currentPage = currentPage,
            totalPages = totalPages,
        )
    }
}
