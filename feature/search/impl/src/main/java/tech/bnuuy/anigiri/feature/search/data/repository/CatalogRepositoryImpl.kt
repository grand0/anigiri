package tech.bnuuy.anigiri.feature.search.data.repository

import tech.bnuuy.anigiri.core.network.datasource.AnimeDataSource
import tech.bnuuy.anigiri.core.network.datasource.response.ReleaseResponse
import tech.bnuuy.anigiri.feature.search.api.data.model.Genre
import tech.bnuuy.anigiri.feature.search.api.data.model.ICatalogSearchFilter
import tech.bnuuy.anigiri.feature.search.api.data.model.PagedContent
import tech.bnuuy.anigiri.feature.search.api.data.model.Release
import tech.bnuuy.anigiri.feature.search.api.data.repository.CatalogRepository
import tech.bnuuy.anigiri.feature.search.data.mapper.mapToDomain
import tech.bnuuy.anigiri.feature.search.data.mapper.toDomain
import tech.bnuuy.anigiri.feature.search.data.mapper.toPagedContent
import tech.bnuuy.anigiri.feature.search.data.model.CatalogSearchFilter

class CatalogRepositoryImpl(
    val source: AnimeDataSource,
) : CatalogRepository {
    
    override suspend fun searchCatalog(filter: ICatalogSearchFilter): PagedContent<Release> {
        return with(filter as CatalogSearchFilter) {
            source.searchCatalog(
                page = page,
                limit = limit,
                genres = genres.map { it.id }.toSet(),
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
