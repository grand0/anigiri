package tech.bnuuy.anigiri.feature.player.api.usecase

import tech.bnuuy.anigiri.feature.player.api.data.model.Episode

interface GetEpisodeUseCase {
    suspend operator fun invoke(id: String): Episode
}