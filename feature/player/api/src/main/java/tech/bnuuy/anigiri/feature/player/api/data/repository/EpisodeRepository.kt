package tech.bnuuy.anigiri.feature.player.api.data.repository

import tech.bnuuy.anigiri.feature.player.api.data.model.Episode

interface EpisodeRepository {
    suspend fun getEpisode(id: String): Episode
}