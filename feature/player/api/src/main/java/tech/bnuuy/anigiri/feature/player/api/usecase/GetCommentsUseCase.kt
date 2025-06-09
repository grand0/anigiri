package tech.bnuuy.anigiri.feature.player.api.usecase

import tech.bnuuy.anigiri.feature.player.api.data.model.Comment

interface GetCommentsUseCase {
    suspend operator fun invoke(episodeId: String): List<Comment>
}