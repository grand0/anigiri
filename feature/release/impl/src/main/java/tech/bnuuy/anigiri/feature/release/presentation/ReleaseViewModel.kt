package tech.bnuuy.anigiri.feature.release.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import tech.bnuuy.anigiri.feature.release.api.usecase.AddToFavoritesUseCase
import tech.bnuuy.anigiri.feature.release.api.usecase.CheckFavoriteReleaseUseCase
import tech.bnuuy.anigiri.feature.release.api.usecase.GetRecommendationsUseCase
import tech.bnuuy.anigiri.feature.release.api.usecase.GetReleaseUseCase
import tech.bnuuy.anigiri.feature.release.api.usecase.RemoveFromFavoritesUseCase
import tech.bnuuy.anigiri.feature.release.presentation.model.ReleaseAction
import tech.bnuuy.anigiri.feature.release.presentation.model.ReleaseState

class ReleaseViewModel(
    private val releaseId: Int,
    private val getReleaseUseCase: GetReleaseUseCase,
    private val checkFavoriteReleaseUseCase: CheckFavoriteReleaseUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
) : ViewModel(), ContainerHost<ReleaseState, Nothing> {
    override val container = container<ReleaseState, Nothing>(ReleaseState()) {
        refresh()
    }
    
    fun dispatch(action: ReleaseAction) {
        when (action) {
            ReleaseAction.AddToFavorites -> addToFavorites()
            ReleaseAction.RemoveFromFavorites -> removeFromFavorites()
            ReleaseAction.Refresh -> refresh()
        }
    }
    
    private fun refresh() = intent {
        getRelease()
        coroutineScope {
            launch { checkFavorite() }
            launch { getRecommendations() }
        }
    }
    
    @OptIn(OrbitExperimental::class)
    private suspend fun getRelease() = subIntent {
        reduce {
            state.copy(
                isLoading = true,
                error = null,
            )
        }
        val result = runCatching { 
            getReleaseUseCase(releaseId)
        }
        reduce {
            state.copy(
                release = result.getOrDefault(state.release),
                isLoading = false,
                error = result.exceptionOrNull(),
            )
        }
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun getRecommendations() = subIntent {
        state.release?.let {
            reduce {
                state.copy(
                    areRecommendationsLoading = true,
                    recommendationsError = null,
                )
            }
            val result = runCatching { getRecommendationsUseCase(it) }
            reduce {
                state.copy(
                    recommendations = result.getOrDefault(state.recommendations),
                    areRecommendationsLoading = false,
                    recommendationsError = result.exceptionOrNull(),
                )
            }
        }
    }
    
    @OptIn(OrbitExperimental::class)
    private suspend fun checkFavorite() = subIntent {
        state.release?.let {
            reduce {
                state.copy(
                    isFavoriteLoading = true,
                    isFavorite = null,
                )
            }
            val result = runCatching {
                checkFavoriteReleaseUseCase(it)
            }
            reduce {
                state.copy(
                    isFavoriteLoading = false,
                    isFavorite = result.getOrNull(),
                )
            }
        }
    }
    
    private fun addToFavorites() = intent {
        state.release?.let {
            reduce {
                state.copy(
                    isFavoriteLoading = true,
                )
            }
            val result = runCatching {
                addToFavoritesUseCase(it)
                true
            }
            reduce {
                state.copy(
                    isFavoriteLoading = false,
                    isFavorite = result.getOrDefault(state.isFavorite),
                )
            }
        }
    }

    private fun removeFromFavorites() = intent {
        state.release?.let {
            reduce {
                state.copy(
                    isFavoriteLoading = true,
                )
            }
            val result = runCatching {
                removeFromFavoritesUseCase(it)
                false
            }
            reduce {
                state.copy(
                    isFavoriteLoading = false,
                    isFavorite = result.getOrDefault(state.isFavorite),
                )
            }
        }
    }
}
