package tech.bnuuy.anigiri.feature.search.api.data.repository

import tech.bnuuy.anigiri.feature.search.api.data.model.CatalogSearchFilter
import tech.bnuuy.anigiri.feature.search.api.data.model.Release

interface CatalogRepository {
    suspend fun searchCatalog(filter: CatalogSearchFilter): List<Release>
}
