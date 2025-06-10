package tech.bnuuy.anigiri.feature.player.presentation

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.LifecycleEffectOnce
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import tech.bnuuy.anigiri.core.designsystem.component.CardElevation
import tech.bnuuy.anigiri.core.designsystem.component.ContentCard
import tech.bnuuy.anigiri.core.designsystem.theme.Typography
import tech.bnuuy.anigiri.core.designsystem.util.LocalSnackbarHostState
import tech.bnuuy.anigiri.feature.player.R
import tech.bnuuy.anigiri.feature.player.api.data.model.Comment
import tech.bnuuy.anigiri.feature.player.api.data.model.Episode
import tech.bnuuy.anigiri.feature.player.presentation.player.MediaPlayer
import tech.bnuuy.anigiri.feature.player.util.findActivity
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

internal class PlayerScreen(val episodeId: String) : Screen {

    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    override fun Content() {
        val nav = LocalNavigator.currentOrThrow
        val snackbarHostState = LocalSnackbarHostState.current

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

        LaunchedEffect(player, state.isReadyToPlay) {
            if (player != null && state.isReadyToPlay) {
                prepareAndPlay(player!!, state.episode!!)
            }
        }

        LifecycleEffectOnce {
            player = initializePlayer(context)
            onDispose {
                player?.release()
                player = null

                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // force to portrait but allow user to rotate to landscape afterwards
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
                }
            }
        }

        vm.collectSideEffect { effect ->
            when (effect) {
                is PlayerSideEffect.SendCommentError -> snackbarHostState.showSnackbar(
                    context.getString(R.string.comment_send_error, effect.error)
                )
                PlayerSideEffect.SendCommentSuccess -> {
                    vm.dispatch(PlayerAction.LoadComments)
                    snackbarHostState.showSnackbar(
                        context.getString(R.string.comment_send_success)
                    )
                }
            }
        }

        val onBackClicked: () -> Unit = { nav.pop() }
        val changeFullscreen: () -> Unit = {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            } else {
                // force to portrait but allow user to rotate to landscape afterwards
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { innerPadding ->
            var columnModifier: Modifier = Modifier
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                columnModifier = columnModifier.padding(innerPadding)
            }
            columnModifier = columnModifier.fillMaxSize()

            Column(columnModifier) {
                var playerModifier: Modifier = Modifier
                var playerControlsModifier: Modifier = Modifier
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    playerModifier = playerModifier
                        .fillMaxWidth()
                        .aspectRatio(PLAYER_PORTRAIT_ASPECT_RATIO)
                } else {
                    playerModifier = Modifier.fillMaxSize()
                    playerControlsModifier = Modifier.safeGesturesPadding()
                }

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

                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    state.episode?.let {
                        EpisodeTitle(
                            episodeTitle = it.name,
                            ordinal = it.ordinal,
                            releaseTitle = it.release?.name,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        CommentsList(
                            comments = state.comments,
                            isLoading = state.areCommentsLoading,
                            error = state.commentsError,
                            onLoadComments = { vm.dispatch(PlayerAction.LoadComments) },
                            isLoadingSending = state.isProfileLoading || state.isCommentSending,
                            isAuthorizedToSend = state.isAuthorized,
                            sendComment = { vm.dispatch(PlayerAction.SendComment(it)) }
                        )
                    }
                }
            }
        }
    }

    private fun initializePlayer(context: Context): Player =
        ExoPlayer.Builder(context).build()

    private fun prepareAndPlay(player: Player, episode: Episode) = player.apply {
        setMediaItem(MediaItem.fromUri(
            episode.mediaStreams.hls1080 ?:
            episode.mediaStreams.hls720 ?:
            episode.mediaStreams.hls480 ?: ""))
        prepare()
        play()
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

    @Composable
    private fun EpisodeTitle(
        episodeTitle: String?,
        ordinal: Int,
        releaseTitle: String?,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier.fillMaxWidth()) {
            val titleText =
                episodeTitle ?: stringResource(R.string.episode_ordinal_title_placeholder, ordinal)
            val subtitleText = if (episodeTitle != null && releaseTitle != null) {
                stringResource(R.string.episode_full_title_placeholder, ordinal, releaseTitle)
            } else if (episodeTitle != null) {
                stringResource(R.string.episode_ordinal_title_placeholder, ordinal)
            } else releaseTitle
            Text(
                titleText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                style = Typography.titleLarge
            )
            subtitleText?.let {
                Box(Modifier.height(8.dp))
                Text(
                    it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    style = Typography.bodyMedium
                )
            }
        }
    }

    @Composable
    private fun CommentsList(
        comments: List<Comment>?,
        isLoading: Boolean = false,
        error: Throwable? = null,
        onLoadComments: () -> Unit,
        isAuthorizedToSend: Boolean,
        isLoadingSending: Boolean,
        sendComment: (String) -> Unit,
    ) {
        var expanded by rememberSaveable { mutableStateOf(false) }
        val boxPadding by animateDpAsState(
            if (expanded) 0.dp else 8.dp
        )
        val headerConstPadding = 16.dp
        val cardElevation by animateDpAsState(
            if (expanded) 0.dp else CardElevation
        )

        var newComment by rememberSaveable { mutableStateOf("") }
        val send = {
            if (newComment.isNotBlank()) {
                sendComment(newComment.trim())
                newComment = ""
            }
        }

        Box(Modifier.padding(horizontal = boxPadding)) {
            ContentCard(
                modifier = Modifier
                    .animateContentSize(),
                elevation = cardElevation,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable() {
                                expanded = !expanded
                                if (expanded && ((comments == null && !isLoading) || error != null)) {
                                    onLoadComments()
                                }
                            }
                            .padding(vertical = 8.dp, horizontal = headerConstPadding - boxPadding)
                    ) {
                        Text(
                            stringResource(R.string.comments),
                            style = Typography.titleMedium,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .weight(1f)
                        )
                        Icon(
                            if (expanded) Icons.Default.KeyboardArrowUp
                                else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                        )
                    }

                    OutlinedTextField(
                        newComment,
                        enabled = isAuthorizedToSend && !isLoadingSending,
                        modifier = Modifier
                            .padding(
                                bottom = 8.dp,
                                start = headerConstPadding - boxPadding,
                                end = headerConstPadding - boxPadding,
                            )
                            .fillMaxWidth(),
                        onValueChange = { newComment = it },
                        singleLine = true,
                        maxLines = 1,
                        placeholder = {
                            if (isAuthorizedToSend) {
                                Text(stringResource(R.string.your_comment))
                            } else {
                                Text(stringResource(R.string.not_authorized))
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = { send() },
                        ),
                        trailingIcon = {
                            if (isLoadingSending) {
                                CircularProgressIndicator()
                            } else {
                                IconButton(
                                    enabled = newComment.isNotBlank() && isAuthorizedToSend,
                                    onClick = { send() },
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.Default.Send,
                                        contentDescription = null,
                                    )
                                }
                            }
                        }
                    )

                    if (expanded) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement =
                                Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator()
                            } else if (error != null) {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(error.toString())
                            } else if (comments != null) {
                                if (comments.isEmpty()) {
                                    Icon(
                                        Icons.AutoMirrored.Default.Comment,
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Text(stringResource(R.string.no_comments))
                                } else {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.spacedBy(
                                            12.dp,
                                            Alignment.Top
                                        ),
                                    ) {
                                        items(comments, key = { it.id }) { comment ->
                                            Comment(
                                                userNickname = comment.user.nickname,
                                                userAvatarUrl = comment.user.avatarUrl,
                                                commentText = comment.text,
                                                createdAt = comment.createdAt,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun Comment(
        userNickname: String,
        userAvatarUrl: String?,
        commentText: String,
        createdAt: Instant,
    ) {
        val avatarPainter = rememberAsyncImagePainter(
            model = userAvatarUrl,
        )
        val avatarPainterState by avatarPainter.state.collectAsState()

        Row(
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            if (avatarPainterState is AsyncImagePainter.State.Success) {
                Image(
                    avatarPainter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape),
                )
            } else {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape),
                )
            }
            Box(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                val createdAtStr = DateTimeFormatter
                    .ofLocalizedDateTime(FormatStyle.SHORT)
                    .format(createdAt.toLocalDateTime(TimeZone.of("UTC+3")).toJavaLocalDateTime())

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(userNickname, style = Typography.titleMedium)
                    Text(createdAtStr, style = Typography.labelMedium)
                }
                Box(Modifier.height(4.dp))
                Text(commentText)
            }
        }
    }

    companion object {
        const val PLAYER_PORTRAIT_ASPECT_RATIO = 16f / 9f
        const val PLAYER_CONTROLS_HIDE_DELAY = 2000L // ms
    }
}
