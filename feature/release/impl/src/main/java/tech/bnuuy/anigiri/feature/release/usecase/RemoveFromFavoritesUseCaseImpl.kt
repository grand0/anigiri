package tech.bnuuy.anigiri.feature.release.usecase

import tech.bnuuy.anigiri.feature.release.api.data.model.Release
import tech.bnuuy.anigiri.feature.release.api.data.repository.ProfileRepository
import tech.bnuuy.anigiri.feature.release.api.usecase.RemoveFromFavoritesUseCase

class RemoveFromFavoritesUseCaseImpl(
    private val profileRepository: ProfileRepository,
) : RemoveFromFavoritesUseCase {
    
    override suspend fun invoke(release: Release) {
        profileRepository.removeFavoriteRelease(release)
    }
}
