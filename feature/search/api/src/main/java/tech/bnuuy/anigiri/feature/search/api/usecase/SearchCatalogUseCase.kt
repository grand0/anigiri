package tech.bnuuy.anigiri.feature.search.api.usecase

import tech.bnuuy.anigiri.feature.search.api.data.model.CatalogSearchFilter
import tech.bnuuy.anigiri.feature.search.api.data.model.PagedContent
import tech.bnuuy.anigiri.feature.search.api.data.model.Release

interface SearchCatalogUseCase {
    suspend operator fun invoke(filter: CatalogSearchFilter): PagedContent<Release>
}
