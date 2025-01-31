package tech.bnuuy.anigiri.feature.search.api.data.model

data class PagedContent<T>(
    val data: List<T>,
    val totalItems: Int,
    val itemsCount: Int,
    val itemsPerPage: Int,
    val currentPage: Int,
    val totalPages: Int,
)
