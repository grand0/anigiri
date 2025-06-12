package tech.bnuuy.anigiri.feature.release.api.usecase

import tech.bnuuy.anigiri.feature.release.api.data.model.ICollectionReleaseType
import tech.bnuuy.anigiri.feature.release.api.data.model.Release

interface CheckCollectionReleaseUseCase {
    suspend operator fun invoke(release: Release): ICollectionReleaseType
}
