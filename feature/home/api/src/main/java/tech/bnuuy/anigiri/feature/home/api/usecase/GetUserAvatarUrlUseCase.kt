package tech.bnuuy.anigiri.feature.home.api.usecase

interface GetUserAvatarUrlUseCase {
    suspend operator fun invoke(): String?
}
