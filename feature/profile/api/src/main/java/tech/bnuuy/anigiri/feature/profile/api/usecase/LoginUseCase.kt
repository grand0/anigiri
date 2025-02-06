package tech.bnuuy.anigiri.feature.profile.api.usecase

import tech.bnuuy.anigiri.feature.profile.api.data.model.Profile

interface LoginUseCase {
    suspend operator fun invoke(login: String, password: String): Profile
}
