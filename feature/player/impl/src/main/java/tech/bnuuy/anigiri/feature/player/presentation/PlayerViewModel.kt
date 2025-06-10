package tech.bnuuy.anigiri.feature.player.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import tech.bnuuy.anigiri.feature.player.api.usecase.CheckAuthorizedUseCase
import tech.bnuuy.anigiri.feature.player.api.usecase.GetCommentsUseCase
import tech.bnuuy.anigiri.feature.player.api.usecase.GetEpisodeUseCase
import tech.bnuuy.anigiri.feature.player.api.usecase.SendCommentUseCase

class PlayerViewModel(
    private val episodeId: String,
    private val getEpisodeUseCase: GetEpisodeUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val checkAuthorizedUseCase: CheckAuthorizedUseCase,
    private val sendCommentUseCase: SendCommentUseCase,
) : ViewModel(), ContainerHost<PlayerState, PlayerSideEffect> {
    override val container = container<PlayerState, PlayerSideEffect>(PlayerState()) {
        load()
    }

    fun dispatch(action: PlayerAction) {
        when (action) {
            PlayerAction.Load -> load()
            PlayerAction.LoadComments -> loadComments()
            is PlayerAction.SendComment -> sendComment(action.comment)
        }
    }

    private fun load() = intent {
        coroutineScope {
            launch { loadEpisode() }
            launch { checkAuthorized() }
        }
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun loadEpisode() = subIntent {
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

    @OptIn(OrbitExperimental::class)
    private suspend fun checkAuthorized() = subIntent {
        reduce {
            state.copy(
                isAuthorized = false,
                isProfileLoading = true,
                profileError = null,
            )
        }
        val result = runCatching { checkAuthorizedUseCase() }
        reduce {
            state.copy(
                isAuthorized = result.getOrDefault(false),
                isProfileLoading = false,
                profileError = result.exceptionOrNull(),
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
        reduce {
            state.copy(
                isCommentSending = true,
            )
        }
        val result = runCatching { sendCommentUseCase(comment, episodeId) }
        if (result.isSuccess) {
            postSideEffect(PlayerSideEffect.SendCommentSuccess)
        } else {
            postSideEffect(PlayerSideEffect.SendCommentError(result.exceptionOrNull()))
        }
        reduce {
            state.copy(isCommentSending = false)
        }
    }
}
