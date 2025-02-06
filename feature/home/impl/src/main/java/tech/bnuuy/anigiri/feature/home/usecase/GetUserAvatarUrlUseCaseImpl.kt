package tech.bnuuy.anigiri.feature.home.usecase

import tech.bnuuy.anigiri.feature.home.api.data.repository.ProfileRepository
import tech.bnuuy.anigiri.feature.home.api.usecase.GetUserAvatarUrlUseCase

class GetUserAvatarUrlUseCaseImpl(
    private val repository: ProfileRepository,
) : GetUserAvatarUrlUseCase {
    override suspend fun invoke(): String? {
        return repository.getProfileAvatarUrl()
    }
}
