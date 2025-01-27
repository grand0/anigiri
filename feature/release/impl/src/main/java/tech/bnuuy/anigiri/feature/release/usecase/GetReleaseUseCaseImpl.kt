package tech.bnuuy.anigiri.feature.release.usecase

import tech.bnuuy.anigiri.feature.release.api.data.model.Release
import tech.bnuuy.anigiri.feature.release.api.data.repository.ReleaseRepository
import tech.bnuuy.anigiri.feature.release.api.usecase.GetReleaseUseCase

internal class GetReleaseUseCaseImpl(
    val repository: ReleaseRepository,
) : GetReleaseUseCase {
    
    override suspend fun invoke(id: Int): Release {
        return repository.getRelease(id)
    }
}
