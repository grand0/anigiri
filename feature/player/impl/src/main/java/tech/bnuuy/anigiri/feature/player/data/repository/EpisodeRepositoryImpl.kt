package tech.bnuuy.anigiri.feature.player.data.repository

import tech.bnuuy.anigiri.core.network.datasource.AnimeDataSource
import tech.bnuuy.anigiri.core.network.datasource.CommentsDataSource
import tech.bnuuy.anigiri.feature.player.api.data.model.Comment
import tech.bnuuy.anigiri.feature.player.api.data.model.Episode
import tech.bnuuy.anigiri.feature.player.api.data.repository.EpisodeRepository
import tech.bnuuy.anigiri.feature.player.data.mapper.mapList
import tech.bnuuy.anigiri.feature.player.data.mapper.toDomain

class EpisodeRepositoryImpl(
    private val animeDataSource: AnimeDataSource,
    private val commentsDataSource: CommentsDataSource,
) : EpisodeRepository {

    override suspend fun getEpisode(id: String): Episode {
        return animeDataSource.getEpisode(id).toDomain()
    }

    override suspend fun getCommentsForEpisode(id: String): List<Comment> {
        return commentsDataSource.getCommentsForEpisode(id).mapList()
    }

    override suspend fun sendCommentForEpisode(text: String, episodeId: String) {
        commentsDataSource.sendCommentForEpisode(text, episodeId)
    }
}