package tech.bnuuy.anigiri.feature.favorites.data.repository

import tech.bnuuy.anigiri.core.network.datasource.AccountsDataSource
import tech.bnuuy.anigiri.core.network.datasource.response.ReleaseResponse
import tech.bnuuy.anigiri.feature.favorites.api.data.model.PagedContent
import tech.bnuuy.anigiri.feature.favorites.api.data.model.Release
import tech.bnuuy.anigiri.feature.favorites.api.data.repository.FavoritesRepository
import tech.bnuuy.anigiri.feature.favorites.data.mapper.toDomain
import tech.bnuuy.anigiri.feature.favorites.data.mapper.toPagedContent

class FavoritesRepositoryImpl(
    private val source: AccountsDataSource,
) : FavoritesRepository {
    
    override suspend fun getFavoritesReleases(page: Int): PagedContent<Release> {
        return source.favoriteReleases(page).toPagedContent(ReleaseResponse::toDomain)
    }
}
