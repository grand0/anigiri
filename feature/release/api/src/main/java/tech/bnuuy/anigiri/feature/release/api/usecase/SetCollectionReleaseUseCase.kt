package tech.bnuuy.anigiri.feature.release.api.usecase

import tech.bnuuy.anigiri.feature.release.api.data.model.ICollectionReleaseType
import tech.bnuuy.anigiri.feature.release.api.data.model.Release

interface SetCollectionReleaseUseCase {
    suspend operator fun invoke(release: Release, releaseType: ICollectionReleaseType)
}
