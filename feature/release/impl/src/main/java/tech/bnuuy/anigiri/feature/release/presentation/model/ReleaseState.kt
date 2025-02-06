package tech.bnuuy.anigiri.feature.release.presentation.model

import tech.bnuuy.anigiri.feature.release.api.data.model.Release

data class ReleaseState(
    val release: Release? = null,
    val isLoading: Boolean = true,
    val error: Throwable? = null,

    val isFavoriteLoading: Boolean = false,
    val isFavorite: Boolean? = null,
)
