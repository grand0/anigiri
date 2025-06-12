package tech.bnuuy.anigiri.feature.release.usecase

import tech.bnuuy.anigiri.feature.release.api.data.model.ICollectionReleaseType
import tech.bnuuy.anigiri.feature.release.api.data.model.Release
import tech.bnuuy.anigiri.feature.release.api.data.repository.ProfileRepository
import tech.bnuuy.anigiri.feature.release.api.usecase.CheckCollectionReleaseUseCase

class CheckCollectionReleaseUseCaseImpl(
    private val profileRepository: ProfileRepository,
) : CheckCollectionReleaseUseCase {
    override suspend fun invoke(release: Release): ICollectionReleaseType {
        return profileRepository.checkCollectionRelease(release)
    }
}
