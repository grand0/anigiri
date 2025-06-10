package tech.bnuuy.anigiri.feature.player.usecase

import tech.bnuuy.anigiri.feature.player.api.data.repository.ProfileRepository
import tech.bnuuy.anigiri.feature.player.api.usecase.CheckAuthorizedUseCase

class CheckAuthorizedUseCaseImpl(
    private val profileRepository: ProfileRepository,
) : CheckAuthorizedUseCase {
    override suspend fun invoke(): Boolean {
        return profileRepository.isAuthorized()
    }
}
