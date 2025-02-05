package tech.bnuuy.anigiri.feature.profile.api.usecase

import tech.bnuuy.anigiri.feature.profile.api.data.model.Profile

interface GetAuthorizedProfileUseCase {
    suspend operator fun invoke(useCache: Boolean = true): Profile?
}
