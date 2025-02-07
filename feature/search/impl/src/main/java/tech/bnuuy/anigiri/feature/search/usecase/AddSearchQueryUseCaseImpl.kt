package tech.bnuuy.anigiri.feature.search.usecase

import tech.bnuuy.anigiri.feature.search.api.data.model.SearchQuery
import tech.bnuuy.anigiri.feature.search.api.data.repository.SearchQueryRepository
import tech.bnuuy.anigiri.feature.search.api.usecase.AddSearchQueryUseCase

class AddSearchQueryUseCaseImpl(
    private val repository: SearchQueryRepository,
) : AddSearchQueryUseCase {
    
    override suspend fun invoke(query: SearchQuery) {
        repository.insert(query)
    }
}
