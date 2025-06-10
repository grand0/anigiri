package tech.bnuuy.anigiri.feature.player.presentation.player

import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.modifiers.resizeWithContentScale
import androidx.media3.ui.compose.state.rememberPresentationState
import kotlinx.coroutines.delay
import tech.bnuuy.anigiri.feature.player.presentation.PlayerScreen.Companion.PLAYER_CONTROLS_HIDE_DELAY

@OptIn(UnstableApi::class)
@Composable
internal fun MediaPlayer(
    player: Player,
    modifier: Modifier = Modifier,
    controlsModifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    isFullscreen: Boolean,
    changeFullscreen: () -> Unit,
) {
    var showControls by remember { mutableStateOf(true) }
    var lastInteractionTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var isSeeking by remember { mutableStateOf(false) }
    val presentationState = rememberPresentationState(player)

    LaunchedEffect(showControls, lastInteractionTime, isSeeking) {
        if (showControls && !isSeeking) {
            delay(PLAYER_CONTROLS_HIDE_DELAY - (System.currentTimeMillis() - lastInteractionTime))
            showControls = false
        }
    }

    Box(modifier.background(Color.Black)) {
        PlayerSurface(
            player,
            modifier = Modifier
                .resizeWithContentScale(
                    ContentScale.Fit,
                    sourceSizeDp = presentationState.videoSizeDp,
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    showControls = !showControls
                    lastInteractionTime = System.currentTimeMillis()
                }
        )
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .then(controlsModifier)
            ) {
                BackButton(
                    onClick = onBackClicked,
                    modifier = Modifier.align(Alignment.TopStart),
                )

                PlayPauseButton(
                    player,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(64.dp)
                        .align(Alignment.Center)
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    var seekingPosition by remember { mutableLongStateOf(0L) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.End),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PlayerTime(
                            player = player,
                            seekingPosition = if (isSeeking) seekingPosition else null
                        )

                        FullscreenButton(
                            onClick = changeFullscreen,
                            isFullscreen = isFullscreen,
                        )
                    }

                    PlayerSeekBar(
                        player = player,
                        onValueChange = {
                            isSeeking = true
                            seekingPosition = it
                        },
                        onValueChangeFinished = {
                            isSeeking = false
                            lastInteractionTime = System.currentTimeMillis()
                        },
                    )
                }
            }
        }
    }
}
