package tech.bnuuy.anigiri.core.network.repository

import tech.bnuuy.anigiri.core.network.datasource.AnimeDataSource
import tech.bnuuy.anigiri.core.network.mapper.mapList
import tech.bnuuy.anigiri.core.network.mapper.toDomain
import tech.bnuuy.anigiri.core.network.model.Release

class AnimeRepository internal constructor(
    private val source: AnimeDataSource,
) {
    suspend fun getRandomRelease(): Release {
        return source.getRandomRelease()[0].toDomain()
    }

    suspend fun getLatestReleases(): List<Release> {
        return source.getLatestReleases().mapList()
    }

    suspend fun getRelease(id: Int): Release {
        return source.getRelease(id).toDomain()
    }
}
