package tech.bnuuy.anigiri.feature.player.presentation.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.media3.common.Player
import androidx.media3.common.listen
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun rememberPlayerPositionState(player: Player): PlayerPositionState {
    val state = remember(player) { PlayerPositionState(player) }
    LaunchedEffect(player) { state.observe() }
    return state
}

class PlayerPositionState(private val player: Player) {
    var isEnabled by mutableStateOf(shouldEnableSeek())
        private set

    var currentPosition by mutableLongStateOf(0)
        private set

    var duration by mutableLongStateOf(0)
        private set

    fun seek(posMs: Long) {
        player.seekTo(posMs)
    }

    suspend fun observe() {
        coroutineScope {
            launch {
                while (true) {
                    currentPosition = if (isEnabled) player.currentPosition else 0
                    duration = if (isEnabled) player.duration else 0
                    println("UPD: $currentPosition $duration")
                    delay(1.seconds / 10)
                }
            }
            launch {
                player.listen { events ->
                    if (events.containsAny(
                            Player.EVENT_POSITION_DISCONTINUITY,
                            Player.EVENT_AVAILABLE_COMMANDS_CHANGED,
                        )
                    ) {
                        isEnabled = shouldEnableSeek()
                        this@PlayerPositionState.currentPosition =
                            if (isEnabled) currentPosition else 0
                        this@PlayerPositionState.duration =
                            if (isEnabled) duration else 0
                        println("GOT EVENT: ${this@PlayerPositionState.currentPosition}")
                    }
                }
            }
        }
    }

    private fun shouldEnableSeek(): Boolean {
        return player.isCommandAvailable(Player.COMMAND_GET_CURRENT_MEDIA_ITEM) &&
                player.isCommandAvailable(Player.COMMAND_SEEK_IN_CURRENT_MEDIA_ITEM)
    }
}
