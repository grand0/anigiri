package tech.bnuuy.anigiri.feature.release.api.data.repository

import tech.bnuuy.anigiri.feature.release.api.data.model.Release

interface ProfileRepository {
    suspend fun checkFavoriteRelease(release: Release): Boolean
    suspend fun addFavoriteRelease(release: Release)
    suspend fun removeFavoriteRelease(release: Release)
}
