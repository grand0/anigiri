package tech.bnuuy.anigiri.feature.player.usecase

import tech.bnuuy.anigiri.feature.player.api.data.model.Episode
import tech.bnuuy.anigiri.feature.player.api.data.repository.EpisodeRepository
import tech.bnuuy.anigiri.feature.player.api.usecase.GetEpisodeUseCase

class GetEpisodeUseCaseImpl(
    private val episodeRepository: EpisodeRepository,
) : GetEpisodeUseCase {
    override suspend fun invoke(id: String): Episode {
        return episodeRepository.getEpisode(id)
    }
}