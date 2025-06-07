package tech.bnuuy.anigiri.feature.player.data.repository

import tech.bnuuy.anigiri.core.network.datasource.AnimeDataSource
import tech.bnuuy.anigiri.feature.player.api.data.model.Episode
import tech.bnuuy.anigiri.feature.player.api.data.repository.EpisodeRepository
import tech.bnuuy.anigiri.feature.player.data.mapper.toDomain

class EpisodeRepositoryImpl(
    private val animeDataSource: AnimeDataSource,
) : EpisodeRepository {

    override suspend fun getEpisode(id: String): Episode {
        return animeDataSource.getEpisode(id).toDomain()
    }
}