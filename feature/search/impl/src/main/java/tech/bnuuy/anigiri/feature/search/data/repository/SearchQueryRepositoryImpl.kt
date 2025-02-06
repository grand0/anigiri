package tech.bnuuy.anigiri.feature.search.data.repository

import tech.bnuuy.anigiri.core.db.datasource.SearchQueryDataSource
import tech.bnuuy.anigiri.feature.search.api.data.model.SearchQuery
import tech.bnuuy.anigiri.feature.search.api.data.repository.SearchQueryRepository
import tech.bnuuy.anigiri.feature.search.data.mapper.mapToDomain
import tech.bnuuy.anigiri.feature.search.data.mapper.toEntity

class SearchQueryRepositoryImpl(
    private val searchQueryDataSource: SearchQueryDataSource,
) : SearchQueryRepository {
    
    override suspend fun getAll(): List<SearchQuery> {
        return searchQueryDataSource.getAll().mapToDomain()
    }

    override suspend fun insert(query: SearchQuery) {
        return searchQueryDataSource.insert(query.toEntity())
    }

    override suspend fun clearAll() {
        searchQueryDataSource.clearAll()
    }
}
