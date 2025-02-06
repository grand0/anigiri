package tech.bnuuy.anigiri.feature.search.api.data.repository

import tech.bnuuy.anigiri.feature.search.api.data.model.SearchQuery

interface SearchQueryRepository {
    suspend fun getAll(): List<SearchQuery>
    suspend fun insert(query: SearchQuery)
    suspend fun clearAll()
}
