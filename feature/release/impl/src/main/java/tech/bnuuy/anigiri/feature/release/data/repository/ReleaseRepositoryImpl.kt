package tech.bnuuy.anigiri.feature.release.data.repository

import tech.bnuuy.anigiri.core.network.model.Release
import tech.bnuuy.anigiri.core.network.repository.AnimeRepository
import tech.bnuuy.anigiri.feature.release.api.data.repository.ReleaseRepository

internal class ReleaseRepositoryImpl(
    val animeRepository: AnimeRepository,
) : ReleaseRepository {
    
    override suspend fun getRelease(id: Int): Release {
        return animeRepository.getRelease(id)
    }
}
