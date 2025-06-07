package tech.bnuuy.anigiri.feature.player.presentation

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import tech.bnuuy.anigiri.feature.player.api.usecase.GetEpisodeUseCase

class PlayerViewModel(
    private val episodeId: String,
    private val getEpisodeUseCase: GetEpisodeUseCase,
) : ViewModel(), ContainerHost<PlayerState, Nothing> {
    override val container = container<PlayerState, Nothing>(PlayerState()) {
        loadEpisode()
    }

    private fun loadEpisode() = intent {
        reduce {
            state.copy(
                isLoading = true,
                error = null,
            )
        }
        val result = runCatching { getEpisodeUseCase(episodeId) }
        reduce {
            state.copy(
                episode = result.getOrDefault(state.episode),
                isLoading = false,
                error = result.exceptionOrNull(),
            )
        }
    }
}