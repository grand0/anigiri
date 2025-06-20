package tech.bnuuy.anigiri.feature.search.usecase

import tech.bnuuy.anigiri.feature.search.api.data.model.ICatalogSearchFilter
import tech.bnuuy.anigiri.feature.search.api.data.model.PagedContent
import tech.bnuuy.anigiri.feature.search.api.data.model.Release
import tech.bnuuy.anigiri.feature.search.api.data.repository.CatalogRepository
import tech.bnuuy.anigiri.feature.search.api.usecase.SearchCatalogUseCase

class SearchCatalogUseCaseImpl(
    private val repository: CatalogRepository,
) : SearchCatalogUseCase {
    
    override suspend fun invoke(filter: ICatalogSearchFilter): PagedContent<Release> {
        return repository.searchCatalog(filter)
    }
}
