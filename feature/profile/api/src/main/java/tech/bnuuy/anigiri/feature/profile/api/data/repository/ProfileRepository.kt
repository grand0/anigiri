package tech.bnuuy.anigiri.feature.profile.api.data.repository

import tech.bnuuy.anigiri.feature.profile.api.data.model.Profile

interface ProfileRepository {
    suspend fun getAuthorizedProfile(useCache: Boolean = true): Profile?
    suspend fun login(login: String, password: String): Profile
    suspend fun logout()
}
