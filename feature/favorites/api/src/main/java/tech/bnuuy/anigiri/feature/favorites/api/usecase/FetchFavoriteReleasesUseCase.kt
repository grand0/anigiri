package tech.bnuuy.anigiri.feature.favorites.api.usecase

import tech.bnuuy.anigiri.feature.favorites.api.data.model.PagedContent
import tech.bnuuy.anigiri.feature.favorites.api.data.model.Release

interface FetchFavoriteReleasesUseCase {
    suspend operator fun invoke(page: Int): PagedContent<Release>
}
