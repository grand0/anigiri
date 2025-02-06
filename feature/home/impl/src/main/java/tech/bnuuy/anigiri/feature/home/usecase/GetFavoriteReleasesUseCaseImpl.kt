package tech.bnuuy.anigiri.feature.home.usecase

import tech.bnuuy.anigiri.feature.home.api.data.model.Release
import tech.bnuuy.anigiri.feature.home.api.data.repository.ProfileRepository
import tech.bnuuy.anigiri.feature.home.api.usecase.GetFavoriteReleasesUseCase

class GetFavoriteReleasesUseCaseImpl(
    private val repository: ProfileRepository,
) : GetFavoriteReleasesUseCase {
    override suspend fun invoke(): List<Release> {
        return repository.getFavoriteReleases()
    }
}
