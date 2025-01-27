package tech.bnuuy.anigiri.feature.home.api.usecase

import tech.bnuuy.anigiri.feature.home.api.data.model.Release

interface GetRandomReleaseUseCase {
    suspend operator fun invoke(): Release
}
