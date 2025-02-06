package tech.bnuuy.anigiri.feature.favorites.api.data.repository

import tech.bnuuy.anigiri.feature.favorites.api.data.model.PagedContent
import tech.bnuuy.anigiri.feature.favorites.api.data.model.Release

interface FavoritesRepository {
    suspend fun getFavoritesReleases(page: Int): PagedContent<Release>
}
