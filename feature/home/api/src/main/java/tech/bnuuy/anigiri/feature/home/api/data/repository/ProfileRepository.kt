package tech.bnuuy.anigiri.feature.home.api.data.repository

import tech.bnuuy.anigiri.feature.home.api.data.model.Release

interface ProfileRepository {
    suspend fun getFavoriteReleases(): List<Release>
    suspend fun getProfileAvatarUrl(): String?
}
