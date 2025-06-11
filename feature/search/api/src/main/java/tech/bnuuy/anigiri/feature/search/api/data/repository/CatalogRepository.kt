package tech.bnuuy.anigiri.feature.search.api.data.repository

import tech.bnuuy.anigiri.feature.search.api.data.model.Genre
import tech.bnuuy.anigiri.feature.search.api.data.model.ICatalogSearchFilter
import tech.bnuuy.anigiri.feature.search.api.data.model.PagedContent
import tech.bnuuy.anigiri.feature.search.api.data.model.Release

interface CatalogRepository {
    suspend fun searchCatalog(filter: ICatalogSearchFilter): PagedContent<Release>
    suspend fun catalogGenres(): List<Genre>
    suspend fun catalogYears(): List<Int>
}
