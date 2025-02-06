package tech.bnuuy.anigiri.feature.search.usecase

import tech.bnuuy.anigiri.feature.search.api.data.repository.SearchQueryRepository
import tech.bnuuy.anigiri.feature.search.api.usecase.ClearSearchQueriesUseCase

class ClearSearchQueriesUseCaseImpl(
    private val repository: SearchQueryRepository,
) : ClearSearchQueriesUseCase {
    
    override suspend fun invoke() {
        repository.clearAll()
    }
}
