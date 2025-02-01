package tech.bnuuy.anigiri.feature.search.usecase

import tech.bnuuy.anigiri.feature.search.api.data.repository.CatalogRepository
import tech.bnuuy.anigiri.feature.search.api.usecase.FetchYearsUseCase

class FetchYearsUseCaseImpl(
    private val repository: CatalogRepository,
) : FetchYearsUseCase {
    
    override suspend fun invoke(): List<Int> {
        return repository.catalogYears()
    }
}
