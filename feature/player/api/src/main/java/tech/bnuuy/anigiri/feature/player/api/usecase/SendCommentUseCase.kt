package tech.bnuuy.anigiri.feature.player.api.usecase

interface SendCommentUseCase {
    suspend operator fun invoke(text: String, episodeId: String)
}