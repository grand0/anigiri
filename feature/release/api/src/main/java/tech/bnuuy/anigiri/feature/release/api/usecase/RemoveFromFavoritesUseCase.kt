package tech.bnuuy.anigiri.feature.release.api.usecase

import tech.bnuuy.anigiri.feature.release.api.data.model.Release

interface RemoveFromFavoritesUseCase {
    suspend operator fun invoke(release: Release)
}
