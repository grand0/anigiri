package tech.bnuuy.anigiri.feature.home.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import tech.bnuuy.anigiri.feature.home.api.usecase.GetFavoriteReleasesUseCase
import tech.bnuuy.anigiri.feature.home.api.usecase.GetLatestReleasesUseCase
import tech.bnuuy.anigiri.feature.home.api.usecase.GetRandomReleaseUseCase
import tech.bnuuy.anigiri.feature.home.api.usecase.GetUserAvatarUrlUseCase
import tech.bnuuy.anigiri.feature.home.presentation.model.HomeAction
import tech.bnuuy.anigiri.feature.home.presentation.model.HomeSideEffect
import tech.bnuuy.anigiri.feature.home.presentation.model.HomeState

internal class HomeViewModel(
    val getRandomReleaseUseCase: GetRandomReleaseUseCase,
    val getLatestReleasesUseCase: GetLatestReleasesUseCase,
    val getFavoriteReleasesUseCase: GetFavoriteReleasesUseCase,
    val getUserAvatarUrlUseCase: GetUserAvatarUrlUseCase,
) : ContainerHost<HomeState, HomeSideEffect>, ViewModel() {
    override val container = container<HomeState, HomeSideEffect>(HomeState()) {
        fetchAll()
    }
    
    fun dispatch(action: HomeAction) {
        when (action) {
            HomeAction.Refresh -> fetchAll()
            HomeAction.FetchRandomRelease -> getRandomRelease()
        }
    }
    
    private fun fetchAll() = intent {
        reduce { state.copy(homeScreenLoading = true) }
        coroutineScope { 
            launch { getLatestReleases() }
            launch { getFavoriteReleases() }
            launch { getUserAvatar() }
        }
        reduce { state.copy(homeScreenLoading = false) }
    }
    
    @OptIn(OrbitExperimental::class)
    private suspend fun getLatestReleases() = subIntent {
        reduce {
            state.copy(
                latestReleasesLoading = true,
                latestReleasesError = null,
            )
        }
        val result = runCatching { 
            getLatestReleasesUseCase()
        }
        reduce { 
            state.copy(
                latestReleases = result.getOrDefault(state.latestReleases),
                latestReleasesLoading = false,
                latestReleasesError = result.exceptionOrNull(),
            )
        }
    }
    
    @OptIn(OrbitExperimental::class)
    private suspend fun getFavoriteReleases() = subIntent { 
        reduce { 
            state.copy(
                favoriteReleasesLoading = true,
                favoriteReleasesError = null,
            )
        }
        val result = runCatching { 
            getFavoriteReleasesUseCase()
        }
        reduce { 
            state.copy(
                favoriteReleases = result.getOrDefault(state.favoriteReleases),
                favoriteReleasesLoading = false,
                favoriteReleasesError = result.exceptionOrNull(),
            )
        }
    }
    
    @OptIn(OrbitExperimental::class)
    private suspend fun getUserAvatar() = subIntent { 
        val result = runCatching { 
            getUserAvatarUrlUseCase()
        }
        reduce { 
            state.copy(
                profileAvatarUrl = result.getOrNull(),
            )
        }
    }

    private fun getRandomRelease() = intent {
        reduce {
            state.copy(isRandomReleaseLoading = true)
        }
        runCatching {
            getRandomReleaseUseCase()
        }.onSuccess { 
            postSideEffect(HomeSideEffect.GoToRelease(it))
        }.onFailure { 
            postSideEffect(HomeSideEffect.ShowError(it))
        }
        reduce {
            state.copy(isRandomReleaseLoading = false)
        }
    }
}
