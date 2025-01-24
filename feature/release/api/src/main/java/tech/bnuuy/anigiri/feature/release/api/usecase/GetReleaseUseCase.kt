package tech.bnuuy.anigiri.feature.release.api.usecase

import tech.bnuuy.anigiri.core.network.model.Release

interface GetReleaseUseCase {
    suspend operator fun invoke(id: Int): Release
}
