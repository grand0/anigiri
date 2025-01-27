package tech.bnuuy.anigiri.feature.release.api.usecase

import tech.bnuuy.anigiri.feature.release.api.data.model.Release

interface GetReleaseUseCase {
    suspend operator fun invoke(id: Int): Release
}
