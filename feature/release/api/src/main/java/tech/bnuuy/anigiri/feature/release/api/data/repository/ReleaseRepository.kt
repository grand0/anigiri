package tech.bnuuy.anigiri.feature.release.api.data.repository

import tech.bnuuy.anigiri.feature.release.api.data.model.Genre
import tech.bnuuy.anigiri.feature.release.api.data.model.Release

interface ReleaseRepository {
    suspend fun getRelease(id: Int): Release
    suspend fun searchLimited(genres: List<Genre>, limit: Int = 16): List<Release>
}
