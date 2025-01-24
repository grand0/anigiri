package tech.bnuuy.anigiri.feature.home.api.usecase

import tech.bnuuy.anigiri.core.network.model.Release

interface GetRandomReleaseUseCase {
    suspend operator fun invoke(): Release
}
