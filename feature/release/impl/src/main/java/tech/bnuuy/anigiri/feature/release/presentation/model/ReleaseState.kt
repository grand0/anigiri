package tech.bnuuy.anigiri.feature.release.presentation.model

import tech.bnuuy.anigiri.feature.release.api.data.model.Release
import tech.bnuuy.anigiri.feature.release.data.model.CollectionReleaseType

data class ReleaseState(
    val release: Release? = null,
    val isLoading: Boolean = true,
    val error: Throwable? = null,

    val isFavoriteLoading: Boolean = false,
    val isFavorite: Boolean? = null,

    val collectionReleaseType: CollectionReleaseType = CollectionReleaseType(authorized = false),
    val isCollectionTypeLoading: Boolean = false,

    val recommendations: List<Release> = emptyList(),
    val areRecommendationsLoading: Boolean = true,
    val recommendationsError: Throwable? = null,
)
