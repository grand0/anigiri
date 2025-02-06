package tech.bnuuy.anigiri.feature.search.api.usecase

import tech.bnuuy.anigiri.feature.search.api.data.model.SearchQuery

interface AddSearchQueryUseCase {
    suspend operator fun invoke(query: SearchQuery)
}
