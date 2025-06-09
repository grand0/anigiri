package tech.bnuuy.anigiri.feature.player.presentation

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import tech.bnuuy.anigiri.feature.player.api.usecase.GetCommentsUseCase
import tech.bnuuy.anigiri.feature.player.api.usecase.GetEpisodeUseCase

class PlayerViewModel(
    private val episodeId: String,
    private val getEpisodeUseCase: GetEpisodeUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
) : ViewModel(), ContainerHost<PlayerState, Nothing> {
    override val container = container<PlayerState, Nothing>(PlayerState()) {
        loadEpisode()
    }

    fun dispatch(action: PlayerAction) {
        when (action) {
            PlayerAction.LoadEpisode -> loadEpisode()
            PlayerAction.LoadComments -> loadComments()
            is PlayerAction.SendComment -> sendComment(action.comment)
        }
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

    private fun loadComments() = intent {
        reduce {
            state.copy(
                areCommentsLoading = true,
                commentsError = null,
            )
        }
        val result = runCatching { getCommentsUseCase(episodeId) }
        reduce {
            state.copy(
                comments = result.getOrDefault(state.comments),
                areCommentsLoading = false,
                commentsError = result.exceptionOrNull(),
            )
        }
    }

    private fun sendComment(comment: String) = intent {
        // TODO: send
    }
}