package tech.bnuuy.anigiri.feature.home.data.repository

import tech.bnuuy.anigiri.core.network.datasource.AccountsDataSource
import tech.bnuuy.anigiri.core.network.session.AppSession
import tech.bnuuy.anigiri.feature.home.api.data.model.Release
import tech.bnuuy.anigiri.feature.home.api.data.repository.ProfileRepository
import tech.bnuuy.anigiri.feature.home.data.mapper.extractAvatarUrl
import tech.bnuuy.anigiri.feature.home.data.mapper.extractData
import tech.bnuuy.anigiri.feature.home.data.mapper.mapToDomain

internal class ProfileRepositoryImpl(
    private val appSession: AppSession,
    private val source: AccountsDataSource,
) : ProfileRepository {
    
    override suspend fun getFavoriteReleases(): List<Release> {
        return try {
            if (appSession.checkAuthorized()) {
                source.favoriteReleases(page = 1, limit = 10).extractData().mapToDomain()
            } else {
                emptyList()
            }
        } catch (_: Throwable) {
            emptyList()
        }
    }

    override suspend fun getProfileAvatarUrl(): String? {
        return try {
            if (appSession.checkAuthorized()) {
                appSession.getAuthorizedUser()?.extractAvatarUrl()
            } else {
                null
            }
        } catch (_: Throwable) {
            null
        }
    }
}
