package tech.bnuuy.anigiri.feature.release.usecase

import tech.bnuuy.anigiri.feature.release.api.data.model.ICollectionReleaseType
import tech.bnuuy.anigiri.feature.release.api.data.model.Release
import tech.bnuuy.anigiri.feature.release.api.data.repository.ProfileRepository
import tech.bnuuy.anigiri.feature.release.api.usecase.SetCollectionReleaseUseCase

class SetCollectionReleaseUseCaseImpl(
    private val profileRepository: ProfileRepository,
) : SetCollectionReleaseUseCase {
    override suspend fun invoke(
        release: Release,
        releaseType: ICollectionReleaseType
    ) {
        profileRepository.setCollectionForRelease(release, releaseType)
    }
}
