package tech.bnuuy.anigiri.feature.home.presentation.model

import tech.bnuuy.anigiri.feature.home.api.data.model.Release

internal data class HomeState(
    val profileAvatarUrl: String? = null,
    val homeScreenLoading: Boolean = true,
    
    val latestReleases: List<Release> = emptyList(),
    val latestReleasesLoading: Boolean = true,
    val latestReleasesError: Throwable? = null,
    
    val favoriteReleases: List<Release> = emptyList(),
    val favoriteReleasesLoading: Boolean = true,
    val favoriteReleasesError: Throwable? = null,
    
    val isRandomReleaseLoading: Boolean = false,
)
