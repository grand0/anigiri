package tech.bnuuy.anigiri.feature.search.data.mapper

import tech.bnuuy.anigiri.core.db.entity.SearchQueryEntity
import tech.bnuuy.anigiri.feature.search.api.data.model.SearchQuery

internal fun List<SearchQueryEntity>.mapToDomain(): List<SearchQuery> =
    map { it.toDomain() }

internal fun SearchQueryEntity.toDomain(): SearchQuery {
    return SearchQuery(
        query = query,
        timestamp = timestamp,
    )
}

internal fun SearchQuery.toEntity(): SearchQueryEntity {
    return SearchQueryEntity(
        query = query,
        timestamp = timestamp,
    )
}
