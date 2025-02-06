package tech.bnuuy.anigiri.feature.profile.data.repository

import tech.bnuuy.anigiri.core.network.session.AppSession
import tech.bnuuy.anigiri.feature.profile.api.data.model.Profile
import tech.bnuuy.anigiri.feature.profile.api.data.repository.ProfileRepository
import tech.bnuuy.anigiri.feature.profile.data.mapper.toDomain

class ProfileRepositoryImpl(
    private val appSession: AppSession,
) : ProfileRepository {
    
    override suspend fun getAuthorizedProfile(useCache: Boolean): Profile? {
        return appSession.getAuthorizedUser(useCache)?.toDomain()
    }

    override suspend fun login(login: String, password: String): Profile {
        return appSession.authorize(login, password).toDomain()
    }

    override suspend fun logout() {
        return appSession.logout()
    }
}
