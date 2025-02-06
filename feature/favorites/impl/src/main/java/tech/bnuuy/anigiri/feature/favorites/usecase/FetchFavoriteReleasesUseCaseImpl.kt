package tech.bnuuy.anigiri.feature.favorites.usecase

import tech.bnuuy.anigiri.feature.favorites.api.data.model.PagedContent
import tech.bnuuy.anigiri.feature.favorites.api.data.model.Release
import tech.bnuuy.anigiri.feature.favorites.api.data.repository.FavoritesRepository
import tech.bnuuy.anigiri.feature.favorites.api.usecase.FetchFavoriteReleasesUseCase

class FetchFavoriteReleasesUseCaseImpl(
    private val repository: FavoritesRepository,
) : FetchFavoriteReleasesUseCase {
    
    override suspend fun invoke(page: Int): PagedContent<Release> {
        return repository.getFavoritesReleases(page)
    }
}
