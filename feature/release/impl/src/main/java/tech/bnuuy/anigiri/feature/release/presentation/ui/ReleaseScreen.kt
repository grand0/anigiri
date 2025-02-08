package tech.bnuuy.anigiri.feature.release.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState
import tech.bnuuy.anigiri.core.designsystem.component.ContentCard
import tech.bnuuy.anigiri.core.designsystem.component.ExpandableCard
import tech.bnuuy.anigiri.core.designsystem.component.ExpandableTextCard
import tech.bnuuy.anigiri.core.designsystem.component.Poster
import tech.bnuuy.anigiri.core.designsystem.theme.Typography
import tech.bnuuy.anigiri.core.designsystem.util.LocalSnackbarHostState
import tech.bnuuy.anigiri.feature.release.R
import tech.bnuuy.anigiri.feature.release.api.data.model.Episode
import tech.bnuuy.anigiri.feature.release.api.data.model.Release
import tech.bnuuy.anigiri.feature.release.presentation.ReleaseViewModel
import tech.bnuuy.anigiri.feature.release.presentation.model.ReleaseAction
import kotlin.time.Duration

val SectionsHeight = 80.dp

class ReleaseScreen(val releaseId: Int) : Screen {
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val nav = LocalNavigator.currentOrThrow
        val snackbarHostState = LocalSnackbarHostState.current
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        
        val vm = koinViewModel<ReleaseViewModel> { parametersOf(releaseId) }
        val state by vm.collectAsState()
        
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                AppBar(
                    onBackPressed = { nav.pop() },
                    showFavoriteButton = state.release != null,
                    onFavoritePressed = {
                        if (state.isFavorite == true) {
                            vm.dispatch(ReleaseAction.RemoveFromFavorites)
                        } else if (state.isFavorite == false) {
                            vm.dispatch(ReleaseAction.AddToFavorites)
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    context.getString(R.string.authorize_to_add_favorites)
                                )
                            }
                        }
                    },
                    isFavorite = state.isFavorite == true,
                    isFavoriteLoading = state.isFavoriteLoading,
                    favoritesCount = state.release?.favorites ?: 0,
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { innerPadding ->
            if (state.error != null) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Text(
                        state.error.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                ReleaseInfo(
                    release = state.release,
                    contentPadding = innerPadding,
                    isRefreshing = state.isLoading,
                    onRefresh = { vm.dispatch(ReleaseAction.Refresh) }
                )
            }
        }
    }

    @Composable
    private fun AppBar(
        onBackPressed: () -> Unit,
        showFavoriteButton: Boolean,
        onFavoritePressed: () -> Unit,
        isFavorite: Boolean,
        isFavoriteLoading: Boolean,
        favoritesCount: Int,
    ) {
        val gradient = Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.surface.copy(alpha = 1.0f),
                MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
                MaterialTheme.colorScheme.surface.copy(alpha = 0.0f),
            )
        )

        Box(
            Modifier
                .background(gradient)
                .safeDrawingPadding()
                .padding(8.dp)
                .fillMaxWidth()
                .height(64.dp),
        ) {
            IconButton(
                onClick = onBackPressed,
                modifier = Modifier.align(Alignment.CenterStart),
            ) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
            }

            if (showFavoriteButton) {
                Row(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("$favoritesCount")
                    Spacer(Modifier.width(8.dp))
                    IconButton(
                        onClick = onFavoritePressed,
                        enabled = !isFavoriteLoading,
                    ) {
                        if (isFavoriteLoading) {
                            CircularProgressIndicator()
                        } else {
                            Icon(
                                if (isFavorite) Icons.Default.Favorite
                                    else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
        }
    }
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ReleaseInfo(
        release: Release?,
        contentPadding: PaddingValues,
        isRefreshing: Boolean,
        onRefresh: () -> Unit,
    ) {
        val posterPainter = rememberAsyncImagePainter(
            model = release?.posterUrl,
            contentScale = ContentScale.Crop,
        )
        val painterState by posterPainter.state.collectAsState()

        val pullToRefreshState = rememberPullToRefreshState()
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            state = pullToRefreshState,
            indicator = {
                Indicator(
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                    threshold = 160.dp
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = contentPadding,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (release != null) {
                    item {
                        Poster(
                            painter = posterPainter,
                            isLoading = painterState is AsyncImagePainter.State.Loading,
                            isError = painterState is AsyncImagePainter.State.Error,
                            modifier = Modifier
                                .width(250.dp)
                                .padding(bottom = 16.dp),
                        )
                    }
                    item {
                        Text(
                            release.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            style = Typography.titleLarge,
                            textAlign = TextAlign.Start,
                        )
                        Spacer(Modifier.height(4.dp))
                    }
                    if (release.nameEng != null) {
                        item {
                            Text(
                                release.nameEng!!,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                style = Typography.labelLarge,
                                textAlign = TextAlign.Start,
                            )
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                    
                    item {
                        Box(Modifier.padding(horizontal = 12.dp)) {
                            CommonInfoCard(release)
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                    
                    if (release.description.isNotBlank()) {
                        item {
                            Box(Modifier.padding(horizontal = 12.dp)) {
                                DescriptionCard(release)
                            }
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                    
                    if ((release.episodesTotal ?: 0) != 0) {
                        item {
                            Box(Modifier.padding(horizontal = 12.dp)) {
                                EpisodesCard(release = release)
                            }
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
    
    @Composable
    private fun CommonInfoCard(release: Release) {
        ContentCard(
            modifier = Modifier.padding(12.dp)
        ) {
            Column {
                HalfSections(
                    leftSection = {
                        Text("${release.year}", style = Typography.titleLarge)
                        Text(stringResource(R.string.year), style = Typography.labelSmall)
                    },
                    rightSection = {
                        Text(release.ageRatingLabel, style = Typography.titleLarge)
                        Text(stringResource(R.string.age_rating), style = Typography.labelSmall)
                    }
                )
                Spacer(Modifier.height(12.dp))

                if (!release.genres.isNullOrEmpty()) {
                    Text(
                        release.genres!!.joinToString(separator = " • ") { it.name },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(12.dp))
                }
                
                val isInProductionStr =
                    if (release.isInProduction) stringResource(R.string.is_in_production)
                    else stringResource(R.string.is_not_in_production)
                val isOngoingStr =
                    if (release.isOngoing) stringResource(R.string.ongoing)
                    else stringResource(R.string.not_ongoing)
                Text(
                    "$isOngoingStr • $isInProductionStr",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
    
    @Composable
    private fun DescriptionCard(release: Release) {
        ExpandableTextCard(
            release.description,
            title = stringResource(R.string.description),
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(8.dp),
            titleModifier = Modifier.padding(vertical = 8.dp)
        )
    }
    
    @Composable
    private fun EpisodesCard(release: Release) {
        ExpandableCard(
            title = stringResource(R.string.episodes),
            modifier = Modifier
                .padding(8.dp),
            titleModifier = Modifier.padding(vertical = 8.dp)
        ) { expanded ->
            val released = release.episodes?.size ?: 0
            val episodesStr =
                if (released != release.episodesTotal) "$released/${release.episodesTotal}"
                else "${release.episodesTotal}"
            if (release.episodeDurationMinutes == null) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .height(SectionsHeight),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(episodesStr, style = Typography.titleLarge)
                    Text(stringResource(R.string.n_episodes), style = Typography.labelSmall)
                }
            } else {
                HalfSections(
                    leftSection = {
                        Text(episodesStr, style = Typography.titleLarge)
                        Text(stringResource(R.string.n_episodes), style = Typography.labelSmall)
                    },
                    rightSection = {
                        Text(
                            stringResource(
                                R.string.n_minutes,
                                release.episodeDurationMinutes ?: 0
                            ),
                            style = Typography.titleLarge
                        )
                        Text(
                            stringResource(R.string.episode_duration),
                            style = Typography.labelSmall
                        )
                    }
                )
            }
            
            if (expanded) {
                for (episode in release.episodes ?: emptyList()) {
                    EpisodeItem(episode)
                }
            }
        }
    }
    
    @Composable
    private fun EpisodeItem(episode: Episode) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                contentAlignment = Alignment.BottomStart,
            ) {
                Text(
                    "${episode.ordinal}",
                    fontSize = 42.sp,
                    modifier = Modifier
                        .alpha(0.2f)
                        .align(Alignment.CenterEnd),
                )
                Column(
                    Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        episode.name ?: episode.nameEng ?: stringResource(R.string.unnamed),
                        modifier = Modifier
                            .fillMaxWidth()
                            .basicMarquee(),
                        style = Typography.titleMedium,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                    )
                    if (episode.nameEng != null && episode.name != null) {
                        Text(
                            episode.nameEng!!,
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = Typography.labelSmall,
                            textAlign = TextAlign.Start,
                        )
                    }
                    Text(
                        episode.duration.format(),
                        modifier = Modifier
                            .fillMaxWidth(),
                        style = Typography.labelSmall,
                        textAlign = TextAlign.Start,
                    )
                }
            }
        }
    }
    
    @Composable
    private fun HalfSections(
        leftSection: @Composable ColumnScope.() -> Unit,
        rightSection: @Composable ColumnScope.() -> Unit,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                leftSection()
            }
            VerticalDivider(
                Modifier
                    .height(SectionsHeight)
                    .padding(horizontal = 12.dp)
            )
            Column(
                Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                rightSection()
            }
        }
    }
    
    private fun Duration.format() = toComponents { hours, minutes, seconds, _ ->
        val minutesStr = "$minutes".padStart(2, '0')
        val secondsStr = "$seconds".padStart(2, '0')
        if (hours == 0L) {
            "${minutesStr}:${secondsStr}"
        } else {
            "${hours}:${minutesStr}:${secondsStr}"
        }
    }
}
