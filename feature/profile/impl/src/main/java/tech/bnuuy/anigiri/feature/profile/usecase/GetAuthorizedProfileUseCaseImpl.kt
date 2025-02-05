package tech.bnuuy.anigiri.feature.profile.usecase

import tech.bnuuy.anigiri.feature.profile.api.data.model.Profile
import tech.bnuuy.anigiri.feature.profile.api.data.repository.ProfileRepository
import tech.bnuuy.anigiri.feature.profile.api.usecase.GetAuthorizedProfileUseCase

class GetAuthorizedProfileUseCaseImpl(
    private val repository: ProfileRepository,
) : GetAuthorizedProfileUseCase {
    override suspend fun invoke(useCache: Boolean): Profile? {
        return repository.getAuthorizedProfile(useCache)
    }
}
