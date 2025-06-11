package tech.bnuuy.anigiri.feature.release.data.repository

import tech.bnuuy.anigiri.core.network.datasource.AnimeDataSource
import tech.bnuuy.anigiri.core.network.datasource.enumeration.Sorting
import tech.bnuuy.anigiri.feature.release.api.data.model.Genre
import tech.bnuuy.anigiri.feature.release.api.data.model.Release
import tech.bnuuy.anigiri.feature.release.api.data.repository.ReleaseRepository
import tech.bnuuy.anigiri.feature.release.data.mapper.mapList
import tech.bnuuy.anigiri.feature.release.data.mapper.toDomain

internal class ReleaseRepositoryImpl(
    val source: AnimeDataSource,
) : ReleaseRepository {
    
    override suspend fun getRelease(id: Int): Release {
        return source.getRelease(id).toDomain()
    }

    override suspend fun searchLimited(
        genres: List<Genre>,
        limit: Int
    ): List<Release> {
        return source.searchCatalog(
            limit = limit,
            genres = genres.map { it.id }.toSet(),
            sorting = Sorting.YEAR_DESC
        ).data.mapList()
    }
}
