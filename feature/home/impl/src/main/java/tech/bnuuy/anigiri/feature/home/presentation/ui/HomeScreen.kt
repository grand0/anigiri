package tech.bnuuy.anigiri.feature.home.presentation.ui

import android.text.format.DateUtils
import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import tech.bnuuy.anigiri.core.designsystem.component.Poster
import tech.bnuuy.anigiri.core.designsystem.plus
import tech.bnuuy.anigiri.core.designsystem.theme.Typography
import tech.bnuuy.anigiri.core.designsystem.util.LocalSnackbarHostState
import tech.bnuuy.anigiri.core.nav.Routes
import tech.bnuuy.anigiri.feature.home.R
import tech.bnuuy.anigiri.feature.home.api.data.model.Release
import tech.bnuuy.anigiri.feature.home.presentation.HomeViewModel
import tech.bnuuy.anigiri.feature.home.presentation.model.HomeAction
import tech.bnuuy.anigiri.feature.home.presentation.model.HomeSideEffect
import kotlin.math.roundToInt
import kotlin.toString

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val nav = LocalNavigator.currentOrThrow
        val goToRelease = { release: Release ->
            nav.push(ScreenRegistry.get(Routes.Release(release.id)))
        }
        val goToSearch = { nav.push(ScreenRegistry.get(Routes.Search)) }
        val goToProfile = { nav.push(ScreenRegistry.get(Routes.Profile)) }
        
        val context = LocalContext.current
        val snackbarHostState = LocalSnackbarHostState.current
        val coroutineScope = rememberCoroutineScope()
        
        val vm = koinViewModel<HomeViewModel>()
        val state by vm.collectAsState()
        
        vm.collectSideEffect { 
            when (it) {
                is HomeSideEffect.GoToRelease -> goToRelease(it.release)
                is HomeSideEffect.ShowError -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            context.getString(
                                R.string.error,
                                it.error.toString(),
                            ),
                        )
                    }
                }
            }
        }

        Scaffold(
            Modifier.fillMaxSize(),
            topBar = {
                AppBar(
                    avatarUrl = state.profileAvatarUrl,
                    goToSearch = goToSearch,
                    goToProfile = goToProfile,
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            HomeList(
                latestReleases = state.latestReleases,
                favoriteReleases = state.favoriteReleases,
                isLoading = state.homeScreenLoading,
                onRefresh = { vm.dispatch(HomeAction.Refresh) },
                contentPadding = innerPadding,
                error = state.latestReleasesError,
                onRandomClick = { vm.dispatch(HomeAction.FetchRandomRelease) },
                isRandomLoading = state.isRandomReleaseLoading,
                goToFavorites = { nav.push(ScreenRegistry.get(Routes.Favorites)) },
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun AppBar(
        avatarUrl: String? = null,
        goToSearch: () -> Unit = {},
        goToProfile: () -> Unit = {},
    ) {
        val gradient = Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.surface.copy(alpha = 1.0f),
                MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
                MaterialTheme.colorScheme.surface.copy(alpha = 0.0f),
            )
        )
        val gapSize = 12.dp

        Row(
            Modifier
                .background(gradient)
                .safeDrawingPadding()
                .padding(gapSize)
                .height(64.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            SearchBox(onClick = goToSearch)
            Spacer(Modifier.width(gapSize))
            ProfileButton(onClick = goToProfile, avatarUrl = avatarUrl)
        }
    }
    
    @Composable
    private fun RowScope.SearchBox(
        onClick: () -> Unit = {},
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .weight(1f)
                .background(
                    MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(8.dp),
                )
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onClick),
        ) {
            Box(
                Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .align(Alignment.Center)
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                )
                Text(
                    stringResource(R.string.search_hint),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
    
    @Composable
    private fun ProfileButton(
        onClick: () -> Unit = {},
        avatarUrl: String? = null,
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .background(
                    MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(8.dp),
                )
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            val avatarPainter = rememberAsyncImagePainter(
                model = avatarUrl,
            )
            val avatarPainterState by avatarPainter.state.collectAsState()

            if (avatarPainterState is AsyncImagePainter.State.Success) {
                Image(
                    avatarPainter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                )
            } else {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = null,
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun HomeList(
        latestReleases: List<Release>,
        favoriteReleases: List<Release>,
        isLoading: Boolean,
        onRefresh: () -> Unit,
        error: Throwable? = null,
        isRandomLoading: Boolean,
        onRandomClick: () -> Unit,
        goToFavorites: () -> Unit,
        contentPadding: PaddingValues = PaddingValues(0.dp),
    ) {
        val pullToRefreshState = rememberPullToRefreshState()
        PullToRefreshBox(
            isRefreshing = isLoading,
            onRefresh = onRefresh,
            state = pullToRefreshState,
            indicator = {
                Indicator(
                    state = pullToRefreshState,
                    isRefreshing = isLoading,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = contentPadding.calculateTopPadding() * 2 / 3),
                )
            }
        ) {
            if (error != null) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(contentPadding + PaddingValues(8.dp))
                ) {
                    Text(
                        error.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            LazyColumn(
                contentPadding = contentPadding,
            ) {
                item {
                    Box(Modifier.padding(16.dp)) {
                        FetchRandomReleaseButton(
                            onClick = onRandomClick,
                            isLoading = isRandomLoading,
                        )
                    }
                }
                
                if (latestReleases.isNotEmpty()) {
                    item {
                        LatestReleasesList(latestReleases)
                    }
                }

                if (favoriteReleases.isNotEmpty()) {
                    item {
                        FavoriteReleasesList(
                            releases = favoriteReleases,
                            goToFavorites = goToFavorites,
                        )
                    }
                }
                
                item {
                    Text(
                        stringResource(R.string.coming_soon),
                        style = Typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 36.dp, bottom = 36.dp),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }

    @Composable
    fun FetchRandomReleaseButton(
        onClick: () -> Unit,
        isLoading: Boolean,
    ) {
        OutlinedButton(
            onClick = onClick,
            enabled = !isLoading,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) { 
                
                if (isLoading) {
                    CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Casino, contentDescription = null)
                }
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.random_release))
            }
        }
    }
    
    @Composable
    private fun LatestReleasesList(releases: List<Release>) {
        ReleasesHorizontalList(
            releases = releases,
            listTitle = {
                Text(
                    stringResource(R.string.new_episodes),
                    style = Typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                )
            },
            releaseLabel = { release ->
                release.latestEpisodePublishTime?.let {
                    val relativeDate = DateUtils.getRelativeTimeSpanString(it.toEpochMilliseconds())
                    Text(
                        relativeDate.toString(),
                        textAlign = TextAlign.Center,
                        style = Typography.labelMedium,
                    )
                }
            },
            contentPadding = PaddingValues(horizontal = 12.dp),
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun FavoriteReleasesList(
        releases: List<Release>,
        goToFavorites: () -> Unit,
    ) {
        val overscrollThreshold = 100.dp
        
        val view = LocalView.current
        val listState = rememberLazyListState()
        val difficulty = remember { Animatable(1f) }
        val overscrollThresholdPx = with (LocalDensity.current) { overscrollThreshold.toPx() }
        var overscrollAmount by remember { mutableFloatStateOf(0f) }
        val overscrollProgress = (overscrollAmount / overscrollThresholdPx).coerceIn(0f, 1f)
        val overscrollProgressWithDifficulty = overscrollProgress * difficulty.value
        var overscrollTriggered by remember { mutableStateOf(false) }

        LaunchedEffect(listState) {
            listState.interactionSource.interactions
                .distinctUntilChanged()
                .collect { interaction ->
                    when(interaction) {
                        is DragInteraction.Start -> {
                            difficulty.animateTo(
                                TriggerLowDifficulty,
                                animationSpec = tween(TriggerDifficultyChangeDuration)
                            )
                        }
                        else -> {
                            difficulty.animateTo(
                                TriggerHighDifficulty,
                                animationSpec = tween(TriggerDifficultyChangeDuration)
                            )
                        }
                    }
                }
        }
        
        if (overscrollProgressWithDifficulty == 1f && !overscrollTriggered) {
            overscrollTriggered = true
            view.performHapticFeedback(
                HapticFeedbackConstants.KEYBOARD_TAP,
                HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            )
            goToFavorites()
        } else if (overscrollProgressWithDifficulty != 1f) {
            overscrollTriggered = false
        }

        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null,
        ) {
            ReleasesHorizontalList(
                modifier = Modifier
                    .customOverscroll(
                        orientation = Orientation.Horizontal,
                        onNewOverscrollAmount = {
                            overscrollAmount = -it
                        },
                    )
                    .offset {
                        IntOffset(
                            x = -overscrollAmount
                                .roundToInt()
                                .coerceAtLeast(0),
                            y = 0,
                        )
                    },
                releases = releases,
                listTitle = {
                    Text(
                        stringResource(R.string.favorites),
                        style = Typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    )
                },
                contentPadding = PaddingValues(
                    start = 12.dp,
                    end = 12.dp + overscrollThreshold * overscrollProgress,
                ),
                suffix = {
                    Box(Modifier.padding(horizontal = 16.dp)) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(48.dp),
                            gapSize = 0.dp,
                            trackColor = Color.Transparent,
                            progress = { overscrollProgressWithDifficulty },
                        )
                        IconButton(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(48.dp),
                            onClick = { goToFavorites() },
                        ) {
                            Icon(Icons.AutoMirrored.Default.ArrowForward, contentDescription = null)
                        }
                    }
                },
                state = listState,
            )
        }
    }

    @Composable
    private fun ReleasesHorizontalList(
        modifier: Modifier = Modifier,
        releases: List<Release>,
        listTitle: @Composable () -> Unit = {},
        releaseLabel: @Composable (Release) -> Unit = {},
        suffix: @Composable (LazyItemScope.() -> Unit) = {},
        state: LazyListState = rememberLazyListState(),
        contentPadding: PaddingValues = PaddingValues(0.dp),
    ) {
        val nav = LocalNavigator.currentOrThrow

        listTitle()

        Box(modifier) {
            LazyRow(
                contentPadding = contentPadding,
                state = state,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items(releases) { release ->
                    val releaseScreen = rememberScreen(Routes.Release(release.id))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                nav.push(releaseScreen)
                            }
                            .padding(8.dp)
                    ) {
                        val posterPainter = rememberAsyncImagePainter(
                            model = release.posterUrl,
                            contentScale = ContentScale.Crop,
                        )
                        val painterState by posterPainter.state.collectAsState()
                        Poster(
                            width = 150.dp,
                            padding = PaddingValues(0.dp),
                            painter = posterPainter,
                            isLoading = painterState is AsyncImagePainter.State.Loading,
                            isError = painterState is AsyncImagePainter.State.Error,
                        )
                        Spacer(Modifier.height(8.dp))
                        releaseLabel(release)
                    }
                }
                
                item {
                    suffix()
                }
            }
        }
    }

    @Composable
    private fun ReleaseItem(release: Release) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            val posterPainter = rememberAsyncImagePainter(
                model = release.posterUrl,
                contentScale = ContentScale.Crop,
            )
            val painterState by posterPainter.state.collectAsState()
            Poster(
                painter = posterPainter,
                isLoading = painterState is AsyncImagePainter.State.Loading,
                isError = painterState is AsyncImagePainter.State.Error,
                modifier = Modifier.padding(8.dp)
            )
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    release.name,
                    style = Typography.titleLarge,
                )
                Text(
                    release.description,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    style = Typography.bodySmall,
                )
            }
        }
    }
}

private const val TriggerLowDifficulty = 1f
private const val TriggerHighDifficulty = 0.25f
private const val TriggerDifficultyChangeDuration = 50
