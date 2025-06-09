package tech.bnuuy.anigiri.feature.player.presentation.player

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import tech.bnuuy.anigiri.core.designsystem.format
import tech.bnuuy.anigiri.feature.player.presentation.state.rememberPlayerPositionState
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White
        )
    }
}

@OptIn(UnstableApi::class)
@Composable
internal fun PlayPauseButton(player: Player, modifier: Modifier = Modifier) {
    val state = rememberPlayPauseButtonState(player)
    val icon = if (state.showPlay) Icons.Default.PlayArrow else Icons.Default.Pause
    IconButton(onClick = state::onClick, modifier = modifier, enabled = state.isEnabled) {
        Icon(icon, contentDescription = null, modifier = modifier, tint = Color.White)
    }
}

@Composable
internal fun FullscreenButton(
    onClick: () -> Unit,
    isFullscreen: Boolean,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onClick, modifier = modifier) {
        val icon = if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen
        Icon(icon, contentDescription = null, tint = Color.White)
    }
}

@Composable
internal fun PlayerSeekBar(
    player: Player,
    onValueChange: (Long) -> Unit,
    onValueChangeFinished: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberPlayerPositionState(player)
    var currentPosition by remember { mutableLongStateOf(0) }
    var isSeeking by remember { mutableStateOf(false) }
    if (!isSeeking) currentPosition = state.currentPosition
    Slider(
        value = currentPosition.toFloat(),
        onValueChange = {
            isSeeking = true
            currentPosition = it.toLong()
            onValueChange(it.toLong())
        },
        onValueChangeFinished = {
            state.seek(currentPosition)
            isSeeking = false
            onValueChangeFinished()
        },
        valueRange = 0f..state.duration.toFloat(),
        enabled = state.isEnabled,
        modifier = modifier.height(16.dp),
    )
}

@Composable
internal fun PlayerTime(
    player: Player,
    seekingPosition: Long? = null,
) {
    val state = rememberPlayerPositionState(player)
    var currentPosition by remember { mutableLongStateOf(0) }
    currentPosition = seekingPosition ?: state.currentPosition

    val currentPositionStr = currentPosition.milliseconds.format()
    val durationStr = state.duration.milliseconds.format()

    Text("$currentPositionStr / $durationStr")
}