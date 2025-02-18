package tech.bnuuy.anigiri.feature.search.data.repository

import tech.bnuuy.anigiri.core.network.datasource.AnimeDataSource
import tech.bnuuy.anigiri.core.network.datasource.response.ReleaseResponse
import tech.bnuuy.anigiri.feature.search.api.data.model.CatalogSearchFilter
import tech.bnuuy.anigiri.feature.search.api.data.model.Genre
import tech.bnuuy.anigiri.feature.search.api.data.model.PagedContent
import tech.bnuuy.anigiri.feature.search.api.data.model.Release
import tech.bnuuy.anigiri.feature.search.api.data.repository.CatalogRepository
import tech.bnuuy.anigiri.feature.search.data.mapper.mapToDomain
import tech.bnuuy.anigiri.feature.search.data.mapper.toDomain
import tech.bnuuy.anigiri.feature.search.data.mapper.toPagedContent

class CatalogRepositoryImpl(
    val source: AnimeDataSource,
) : CatalogRepository {
    
    override suspend fun searchCatalog(filter: CatalogSearchFilter): PagedContent<Release> {
        return with(filter) {
            source.searchCatalog(
                page = page,
                limit = limit,
                genres = genres,
                types = types,
                seasons = seasons,
                fromYear = fromYear,
                toYear = toYear,
                search = search,
                sorting = sorting,
                ageRatings = ageRatings,
                publishStatus = publishStatus,
                productionStatus = productionStatus,
            ).toPagedContent(ReleaseResponse::toDomain)
        }
    }

    override suspend fun catalogGenres(): List<Genre> {
        return source.catalogGenres().mapToDomain()
    }

    override suspend fun catalogYears(): List<Int> {
        return source.catalogYears()
    }
}
