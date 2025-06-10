package tech.bnuuy.anigiri.feature.release.usecase

import tech.bnuuy.anigiri.feature.release.api.data.model.Release
import tech.bnuuy.anigiri.feature.release.api.data.repository.ReleaseRepository
import tech.bnuuy.anigiri.feature.release.api.usecase.GetRecommendationsUseCase

class GetRecommendationsUseCaseImpl(
    private val releaseRepository: ReleaseRepository,
) : GetRecommendationsUseCase {
    override suspend fun invoke(release: Release): List<Release> {
        val genres = release.genres?.take(GENRES_TO_SEARCH) ?: emptyList()
        return if (genres.isEmpty()) {
            emptyList()
        } else {
            releaseRepository
                .searchLimited(genres, limit = RECOMMENDATIONS_TO_RETURN)
                .filter { it.id != release.id }
                .take(RECOMMENDATIONS_TO_RETURN)
        }
    }

    companion object {
        const val GENRES_TO_SEARCH = 2
        const val RECOMMENDATIONS_TO_RETURN = 15
    }
}
