package tech.bnuuy.anigiri.feature.player.presentation

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.modifiers.resizeWithContentScale
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import androidx.media3.ui.compose.state.rememberPresentationState
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState
import tech.bnuuy.anigiri.feature.player.api.data.model.Episode
import tech.bnuuy.anigiri.feature.player.presentation.state.rememberPlayerPositionState
import tech.bnuuy.anigiri.feature.player.util.findActivity

internal class PlayerScreen(val episodeId: String) : Screen {

    @Composable
    override fun Content() {
        val nav = LocalNavigator.currentOrThrow

        val vm = koinViewModel<PlayerViewModel> { parametersOf(episodeId) }
        val state by vm.collectAsState()

        val context = LocalContext.current
        val activity = context.findActivity() ?: return
        var player by remember { mutableStateOf<Player?>(null) }

        val configuration = LocalConfiguration.current
        var orientation by remember { mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT) }

        LaunchedEffect(configuration) {
            snapshotFlow { configuration.orientation }
                .collect { orientation = it }
        }

        DisposableEffect(orientation) {
            val window = activity.window
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)

            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                insetsController.apply {
                    hide(WindowInsetsCompat.Type.statusBars())
                    hide(WindowInsetsCompat.Type.navigationBars())
                    systemBarsBehavior =
                        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }

            onDispose {
                insetsController.apply {
                    show(WindowInsetsCompat.Type.statusBars())
                    show(WindowInsetsCompat.Type.navigationBars())
                    systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
                }
            }
        }

        LifecycleStartEffect(state.isReadyToPlay) {
            if (state.isReadyToPlay) {
                player = initializePlayer(context, state.episode!!)
            }
            onStopOrDispose {
                player?.release()
                player = null
            }
        }

        val onBackClicked: () -> Unit = { nav.pop() }
        val changeFullscreen: () -> Unit = {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
            } else {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            val playerModifier = Modifier
                .fillMaxWidth()
                .aspectRatio(PLAYER_PORTRAIT_ASPECT_RATIO)
            Column(Modifier
                .statusBarsPadding()
                .fillMaxSize()) {
                if (player != null) {
                    player?.let {
                        MediaPlayer(
                            it,
                            modifier = playerModifier,
                            onBackClicked = onBackClicked,
                            isFullscreen = orientation == Configuration.ORIENTATION_LANDSCAPE,
                            changeFullscreen = changeFullscreen,
                        )
                    }
                } else {
                    MediaPlayerPlaceholder(
                        modifier = playerModifier,
                        isLoading = state.isLoading,
                        error = state.error
                    )
                }
                Text("Some text")
                Text("Some text")
                Text("Some text")
            }
        } else {
            val playerModifier = Modifier.fillMaxSize()
            val playerControlsModifier = Modifier.safeGesturesPadding()
            if (player != null) {
                player?.let {
                    MediaPlayer(
                        it,
                        modifier = playerModifier,
                        controlsModifier = playerControlsModifier,
                        onBackClicked = onBackClicked,
                        isFullscreen = orientation == Configuration.ORIENTATION_LANDSCAPE,
                        changeFullscreen = changeFullscreen,
                    )
                }
            } else {
                MediaPlayerPlaceholder(
                    modifier = playerModifier,
                    isLoading = state.isLoading,
                    error = state.error
                )
            }
        }
    }

    private fun initializePlayer(context: Context, episode: Episode): Player =
        ExoPlayer.Builder(context).build().apply {
            // TODO: default quality choice? + error if no stream is available
            setMediaItem(MediaItem.fromUri(
                episode.mediaStreams.hls1080 ?:
                episode.mediaStreams.hls720 ?:
                episode.mediaStreams.hls480 ?: ""))
            prepare()
            play()
        }

    @OptIn(UnstableApi::class)
    @Composable
    private fun MediaPlayer(
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
                    IconButton(onClick = onBackClicked, modifier = Modifier.align(Alignment.TopStart)) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    Row(
                        modifier = modifier.align(Alignment.Center),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val buttonModifier = Modifier.size(48.dp)
                        PlayPauseButton(player, buttonModifier)
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        IconButton(onClick = changeFullscreen, modifier = Modifier.align(Alignment.End)) {
                            val icon = if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen
                            Icon(icon, contentDescription = null, tint = Color.White)
                        }

                        PlayerSeekBar(
                            player = player,
                            onValueChange = { isSeeking = true },
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

    @OptIn(UnstableApi::class)
    @Composable
    private fun PlayPauseButton(player: Player, modifier: Modifier = Modifier) {
        val state = rememberPlayPauseButtonState(player)
        val icon = if (state.showPlay) Icons.Default.PlayArrow else Icons.Default.Pause
        IconButton(onClick = state::onClick, modifier = modifier, enabled = state.isEnabled) {
            Icon(icon, contentDescription = null, modifier = modifier, tint = Color.White)
        }
    }

    @Composable
    private fun PlayerSeekBar(
        player: Player,
        onValueChange: (Long) -> Unit,
        onValueChangeFinished: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val state = rememberPlayerPositionState(player)
        var currentPosition by remember { mutableLongStateOf(0) }
        var isSeeking by remember { mutableStateOf(false) }
        if (!isSeeking) currentPosition = state.currentPosition
        println("SLIDER RECOMPOSITION: $currentPosition ${state.duration}")
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
    private fun MediaPlayerPlaceholder(
        modifier: Modifier = Modifier,
        isLoading: Boolean = true,
        error: Throwable? = null,
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else if (error != null) {
                Column(
                    Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null)
                    Text(error.toString())
                }
            }
        }
    }

    companion object {
        const val PLAYER_PORTRAIT_ASPECT_RATIO = 16f / 9f
        const val PLAYER_CONTROLS_HIDE_DELAY = 2000L // ms
    }
}
