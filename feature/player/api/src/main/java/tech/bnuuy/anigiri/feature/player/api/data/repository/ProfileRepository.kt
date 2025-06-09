package tech.bnuuy.anigiri.feature.player.api.data.repository

interface ProfileRepository {
    suspend fun isAuthorized(): Boolean
}