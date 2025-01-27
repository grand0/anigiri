package tech.bnuuy.anigiri.feature.home.usecase

import tech.bnuuy.anigiri.feature.home.api.data.model.Release
import tech.bnuuy.anigiri.feature.home.api.data.repository.ReleaseRepository
import tech.bnuuy.anigiri.feature.home.api.usecase.GetRandomReleaseUseCase

internal class GetRandomReleaseUseCaseImpl(
    private val repository: ReleaseRepository,
) : GetRandomReleaseUseCase {
    
    override suspend fun invoke(): Release {
        return repository.getRandomRelease()
    }
}
