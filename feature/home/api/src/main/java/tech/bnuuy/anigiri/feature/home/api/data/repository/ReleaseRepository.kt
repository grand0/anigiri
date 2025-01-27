package tech.bnuuy.anigiri.feature.home.api.data.repository

import tech.bnuuy.anigiri.feature.home.api.data.model.Release

interface ReleaseRepository {
    suspend fun getRandomRelease(): Release
    suspend fun getLatestReleases(): List<Release>
}
