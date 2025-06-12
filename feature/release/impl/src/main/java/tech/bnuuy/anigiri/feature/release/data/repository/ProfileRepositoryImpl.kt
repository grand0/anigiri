package tech.bnuuy.anigiri.feature.release.data.repository

import tech.bnuuy.anigiri.core.network.datasource.AccountsDataSource
import tech.bnuuy.anigiri.core.network.datasource.request.CollectionReleaseId
import tech.bnuuy.anigiri.core.network.datasource.request.ReleaseId
import tech.bnuuy.anigiri.core.network.exception.NotAuthorizedException
import tech.bnuuy.anigiri.feature.release.api.data.model.ICollectionReleaseType
import tech.bnuuy.anigiri.feature.release.api.data.model.Release
import tech.bnuuy.anigiri.feature.release.api.data.repository.ProfileRepository
import tech.bnuuy.anigiri.feature.release.data.mapper.toId
import tech.bnuuy.anigiri.feature.release.data.model.CollectionReleaseType

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

    override suspend fun checkCollectionRelease(release: Release): ICollectionReleaseType {
        return try {
            accountsDataSource.collectionReleasesIds()
                .firstOrNull { it.id == release.id }?.let {
                    CollectionReleaseType(type = it.collectionType)
                } ?: CollectionReleaseType()
        } catch (_: NotAuthorizedException) {
            CollectionReleaseType(authorized = false)
        }
    }

    override suspend fun setCollectionForRelease(
        release: Release,
        releaseType: ICollectionReleaseType
    ) {
        val releaseType = releaseType as CollectionReleaseType
        if (releaseType.type != null) {
            accountsDataSource.addToCollection(listOf(CollectionReleaseId(
                id = release.id,
                collectionType = releaseType.type,
            )))
        } else {
            accountsDataSource.removeFromCollection(listOf(ReleaseId(release.id)))
        }
    }
}
