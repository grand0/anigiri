package tech.bnuuy.anigiri.feature.player.api.usecase

interface CheckAuthorizedUseCase {
    suspend operator fun invoke(): Boolean
}