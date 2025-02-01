package tech.bnuuy.anigiri.feature.search.usecase

import tech.bnuuy.anigiri.feature.search.api.data.model.Genre
import tech.bnuuy.anigiri.feature.search.api.data.repository.CatalogRepository
import tech.bnuuy.anigiri.feature.search.api.usecase.FetchGenresUseCase

class FetchGenresUseCaseImpl(
    private val repository: CatalogRepository,
) : FetchGenresUseCase {
    
    override suspend fun invoke(): List<Genre> {
        return repository.catalogGenres()
    }
}
