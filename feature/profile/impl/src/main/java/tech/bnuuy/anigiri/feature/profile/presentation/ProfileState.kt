package tech.bnuuy.anigiri.feature.profile.presentation

import tech.bnuuy.anigiri.feature.profile.api.data.model.Profile

data class ProfileState(
    val isFirstLoad: Boolean = true,
    val profile: Profile? = null,
    val isProfileLoading: Boolean = false,
    val profileError: Throwable? = null,
    val isLoggingIn: Boolean = false,
    val loginError: Throwable? = null,
    val isLoggingOut: Boolean = false,
    val logoutError: Throwable? = null,
)
