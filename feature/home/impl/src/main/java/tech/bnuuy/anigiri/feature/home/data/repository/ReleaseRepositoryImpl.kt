package tech.bnuuy.anigiri.feature.home.data.repository

import tech.bnuuy.anigiri.core.network.model.Release
import tech.bnuuy.anigiri.core.network.repository.AnimeRepository
import tech.bnuuy.anigiri.feature.home.api.data.repository.ReleaseRepository

internal class ReleaseRepositoryImpl(
    val animeRepository: AnimeRepository,
) : ReleaseRepository {
    
    override suspend fun getRandomRelease(): Release {
        return animeRepository.getRandomRelease()
    }

    override suspend fun getLatestReleases(): List<Release> {
        return animeRepository.getLatestReleases()
    }
}
