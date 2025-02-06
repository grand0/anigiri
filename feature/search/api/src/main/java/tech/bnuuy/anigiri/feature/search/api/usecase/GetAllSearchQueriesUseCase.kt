package tech.bnuuy.anigiri.feature.search.api.usecase

import tech.bnuuy.anigiri.feature.search.api.data.model.SearchQuery

interface GetAllSearchQueriesUseCase {
    suspend operator fun invoke(): List<SearchQuery>
}
