package tech.bnuuy.anigiri.feature.release.data.repository

import tech.bnuuy.anigiri.core.network.datasource.AccountsDataSource
import tech.bnuuy.anigiri.feature.release.api.data.model.Release
import tech.bnuuy.anigiri.feature.release.api.data.repository.ProfileRepository
import tech.bnuuy.anigiri.feature.release.data.mapper.toId

class ProfileRepositoryImpl(
    private val accountsDataSource: AccountsDataSource,
) : ProfileRepository {

    override suspend fun checkFavoriteRelease(release: Release): Boolean {
        return accountsDataSource.favoriteReleasesIds().contains(release.id)
    }

    override suspend fun addFavoriteRelease(release: Release) {
        accountsDataSource.addFavoriteReleases(listOf(release.toId()))
    }

    override suspend fun removeFavoriteRelease(release: Release) {
        accountsDataSource.removeFavoriteReleases(listOf(release.toId()))
    }
}
