package tech.bnuuy.anigiri.feature.release.presentation.model

import tech.bnuuy.anigiri.core.network.model.Release

data class ReleaseState(
    val release: Release? = null,
    val isLoading: Boolean = true,
    val error: Throwable? = null,
)
