package tech.bnuuy.anigiri.feature.home.usecase

import tech.bnuuy.anigiri.core.network.model.Release
import tech.bnuuy.anigiri.feature.home.api.data.repository.ReleaseRepository
import tech.bnuuy.anigiri.feature.home.api.usecase.GetLatestReleasesUseCase

class GetLatestReleasesUseCaseImpl(
    private val repository: ReleaseRepository,
) : GetLatestReleasesUseCase {
    
    override suspend fun invoke(): List<Release> {
        return repository.getLatestReleases().sortedByDescending { it.latestEpisode?.updatedAt }
    }
}
