package tech.bnuuy.anigiri.feature.search.usecase

import tech.bnuuy.anigiri.feature.search.api.data.model.SearchQuery
import tech.bnuuy.anigiri.feature.search.api.data.repository.SearchQueryRepository
import tech.bnuuy.anigiri.feature.search.api.usecase.GetAllSearchQueriesUseCase

class GetAllSearchQueriesUseCaseImpl(
    private val repository: SearchQueryRepository,
) : GetAllSearchQueriesUseCase {
    
    override suspend fun invoke(): List<SearchQuery> {
        return repository.getAll()
    }
}
