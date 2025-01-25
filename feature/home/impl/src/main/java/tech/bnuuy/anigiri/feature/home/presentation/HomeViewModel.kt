package tech.bnuuy.anigiri.feature.home.presentation

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import tech.bnuuy.anigiri.feature.home.api.usecase.GetLatestReleasesUseCase
import tech.bnuuy.anigiri.feature.home.api.usecase.GetRandomReleaseUseCase
import tech.bnuuy.anigiri.feature.home.presentation.model.HomeAction
import tech.bnuuy.anigiri.feature.home.presentation.model.HomeSideEffect
import tech.bnuuy.anigiri.feature.home.presentation.model.HomeState

internal class HomeViewModel(
    val getRandomReleaseUseCase: GetRandomReleaseUseCase,
    val getLatestReleasesUseCase: GetLatestReleasesUseCase,
) : ContainerHost<HomeState, HomeSideEffect>, ViewModel() {
    override val container = container<HomeState, HomeSideEffect>(HomeState())
    
    init {
        getLatestReleases()
    }
    
    fun dispatch(action: HomeAction) {
        when (action) {
            HomeAction.Refresh -> getLatestReleases()
            HomeAction.FetchRandomRelease -> getRandomRelease()
        }
    }
    
    private fun getLatestReleases() = intent {
        latestReleasesLoadingState()
        
        val result = runCatching { 
            getLatestReleasesUseCase()
        }
        reduce { 
            state.copy(
                latestReleases = result.getOrDefault(state.latestReleases),
                isLoading = false,
                error = result.exceptionOrNull(),
            )
        }
    }
    
    @OptIn(OrbitExperimental::class)
    private suspend fun latestReleasesLoadingState() = subIntent {
        reduce {
            state.copy(
                isLoading = true,
                error = null,
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
