package tech.bnuuy.anigiri.feature.player.usecase

import tech.bnuuy.anigiri.feature.player.api.data.repository.EpisodeRepository
import tech.bnuuy.anigiri.feature.player.api.usecase.SendCommentUseCase

class SendCommentUseCaseImpl(
    private val episodeRepository: EpisodeRepository,
) : SendCommentUseCase {
    override suspend fun invoke(text: String, episodeId: String) {
        episodeRepository.sendCommentForEpisode(text, episodeId)
    }
}