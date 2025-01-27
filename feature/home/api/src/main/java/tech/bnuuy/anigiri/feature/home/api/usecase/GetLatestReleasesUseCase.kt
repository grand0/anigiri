package tech.bnuuy.anigiri.feature.home.api.usecase

import tech.bnuuy.anigiri.feature.home.api.data.model.Release

interface GetLatestReleasesUseCase {
    suspend operator fun invoke(): List<Release>
}
