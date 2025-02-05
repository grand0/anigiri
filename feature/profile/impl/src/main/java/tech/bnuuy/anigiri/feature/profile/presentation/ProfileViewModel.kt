package tech.bnuuy.anigiri.feature.profile.presentation

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import tech.bnuuy.anigiri.feature.profile.api.usecase.GetAuthorizedProfileUseCase
import tech.bnuuy.anigiri.feature.profile.api.usecase.LoginUseCase
import tech.bnuuy.anigiri.feature.profile.api.usecase.LogoutUseCase

internal class ProfileViewModel(
    private val getAuthorizedProfileUseCase: GetAuthorizedProfileUseCase,
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ContainerHost<ProfileState, Nothing>, ViewModel() {
    override val container = container<ProfileState, Nothing>(ProfileState()) {
        loadMyProfile()
    }
    
    fun dispatch(action: ProfileAction) {
        when(action) {
            is ProfileAction.Login -> login(action.login, action.password)
            ProfileAction.Logout -> logout()
            ProfileAction.Refresh -> loadMyProfile(useCache = false)
        }
    }
    
    private fun loadMyProfile(useCache: Boolean = true) = intent {
        reduce {
            state.copy(isProfileLoading = true)
        }
        val result = runCatching { 
            getAuthorizedProfileUseCase(useCache)
        }
        reduce { 
            state.copy(
                isFirstLoad = false,
                profile = if (result.isFailure) state.profile else result.getOrNull(),
                isProfileLoading = false,
                profileError = result.exceptionOrNull(),
            )
        }
    }
    
    private fun login(login: String, password: String) = intent {
        reduce { 
            state.copy(isLoggingIn = true)
        }
        val result = runCatching { 
            loginUseCase(login, password)
        }
        reduce {
            state.copy(
                profile = result.getOrNull(),
                isLoggingIn = false,
                loginError = result.exceptionOrNull(),
            )
        }
    }
    
    private fun logout() = intent {
        reduce { 
            state.copy(isLoggingOut = true)
        }
        val result = runCatching { 
            logoutUseCase()
        }
        reduce {
            state.copy(
                profile = if (result.isFailure) state.profile else null,
                isLoggingOut = false,
                logoutError = result.exceptionOrNull(),
            )
        }
    }
}
