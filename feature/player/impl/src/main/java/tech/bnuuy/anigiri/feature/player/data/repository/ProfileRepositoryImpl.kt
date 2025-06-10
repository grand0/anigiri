package tech.bnuuy.anigiri.feature.player.data.repository

import tech.bnuuy.anigiri.core.network.session.AppSession
import tech.bnuuy.anigiri.feature.player.api.data.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val appSession: AppSession,
) : ProfileRepository {
    override suspend fun isAuthorized(): Boolean {
        return appSession.checkAuthorized()
    }
}
