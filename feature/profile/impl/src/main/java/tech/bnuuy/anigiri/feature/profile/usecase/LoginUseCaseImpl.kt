package tech.bnuuy.anigiri.feature.profile.usecase

import tech.bnuuy.anigiri.feature.profile.api.data.model.Profile
import tech.bnuuy.anigiri.feature.profile.api.data.repository.ProfileRepository
import tech.bnuuy.anigiri.feature.profile.api.usecase.LoginUseCase

class LoginUseCaseImpl(
    private val repository: ProfileRepository,
) : LoginUseCase {
    
    override suspend fun invoke(login: String, password: String): Profile {
        return repository.login(login, password)
    }
}
