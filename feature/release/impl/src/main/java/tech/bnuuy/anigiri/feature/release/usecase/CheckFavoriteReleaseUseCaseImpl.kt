package tech.bnuuy.anigiri.feature.release.usecase

import tech.bnuuy.anigiri.feature.release.api.data.model.Release
import tech.bnuuy.anigiri.feature.release.api.data.repository.ProfileRepository
import tech.bnuuy.anigiri.feature.release.api.usecase.CheckFavoriteReleaseUseCase

class CheckFavoriteReleaseUseCaseImpl(
    private val profileRepository: ProfileRepository,
) : CheckFavoriteReleaseUseCase {
    
    override suspend fun invoke(release: Release): Boolean {
        return profileRepository.checkFavoriteRelease(release)
    }
}
