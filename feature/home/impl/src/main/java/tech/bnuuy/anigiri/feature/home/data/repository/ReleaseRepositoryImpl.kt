package tech.bnuuy.anigiri.feature.home.data.repository

import tech.bnuuy.anigiri.core.network.datasource.AnimeDataSource
import tech.bnuuy.anigiri.feature.home.api.data.model.Release
import tech.bnuuy.anigiri.feature.home.api.data.repository.ReleaseRepository
import tech.bnuuy.anigiri.feature.home.data.mapper.mapToDomain
import tech.bnuuy.anigiri.feature.home.data.mapper.toDomain

internal class ReleaseRepositoryImpl(
    val source: AnimeDataSource,
) : ReleaseRepository {
    
    override suspend fun getRandomRelease(): Release {
        return source.getRandomRelease()[0].toDomain()
    }

    override suspend fun getLatestReleases(): List<Release> {
        return source.getLatestReleases().mapToDomain()
    }
}
