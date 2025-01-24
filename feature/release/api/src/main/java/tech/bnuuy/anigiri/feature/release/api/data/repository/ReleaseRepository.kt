package tech.bnuuy.anigiri.feature.release.api.data.repository

import tech.bnuuy.anigiri.core.network.model.Release

interface ReleaseRepository {
    suspend fun getRelease(id: Int): Release
}
