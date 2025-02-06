package tech.bnuuy.anigiri.feature.release.usecase

import tech.bnuuy.anigiri.feature.release.api.data.model.Release
import tech.bnuuy.anigiri.feature.release.api.data.repository.ProfileRepository
import tech.bnuuy.anigiri.feature.release.api.usecase.AddToFavoritesUseCase

class AddToFavoritesUseCaseImpl(
    private val profileRepository: ProfileRepository,
) : AddToFavoritesUseCase {
    
    override suspend fun invoke(release: Release) {
        profileRepository.addFavoriteRelease(release)
    }
}
