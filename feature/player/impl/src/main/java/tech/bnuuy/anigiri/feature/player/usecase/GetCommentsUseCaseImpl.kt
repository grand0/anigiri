package tech.bnuuy.anigiri.feature.player.usecase

import tech.bnuuy.anigiri.feature.player.api.data.model.Comment
import tech.bnuuy.anigiri.feature.player.api.data.repository.EpisodeRepository
import tech.bnuuy.anigiri.feature.player.api.usecase.GetCommentsUseCase

class GetCommentsUseCaseImpl(
    private val episodeRepository: EpisodeRepository,
) : GetCommentsUseCase {
    override suspend fun invoke(episodeId: String): List<Comment> {
        return episodeRepository.getCommentsForEpisode(episodeId)
    }
}