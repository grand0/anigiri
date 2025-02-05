package tech.bnuuy.anigiri.feature.profile.usecase

import tech.bnuuy.anigiri.feature.profile.api.data.repository.ProfileRepository
import tech.bnuuy.anigiri.feature.profile.api.usecase.LogoutUseCase

class LogoutUseCaseImpl(
    private val repository: ProfileRepository,
) : LogoutUseCase {
    
    override suspend fun invoke() {
        return repository.logout()
    }
}
