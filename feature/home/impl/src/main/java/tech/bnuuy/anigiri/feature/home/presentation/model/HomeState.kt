package tech.bnuuy.anigiri.feature.home.presentation.model

import tech.bnuuy.anigiri.core.network.model.Release

internal data class HomeState(
    val latestReleases: List<Release> = emptyList(),
    val isLoading: Boolean = true,
    val error: Throwable? = null,
)
