package tech.bnuuy.anigiri.feature.home.presentation.ui

import android.text.format.DateUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import tech.bnuuy.anigiri.core.designsystem.component.ShimmerLoader
import tech.bnuuy.anigiri.core.designsystem.plus
import tech.bnuuy.anigiri.core.designsystem.theme.Typography
import tech.bnuuy.anigiri.core.designsystem.util.LocalSnackbarHostState
import tech.bnuuy.anigiri.core.nav.Routes
import tech.bnuuy.anigiri.feature.home.api.data.model.Release
import tech.bnuuy.anigiri.feature.home.R
import tech.bnuuy.anigiri.feature.home.presentation.HomeViewModel
import tech.bnuuy.anigiri.feature.home.presentation.model.HomeAction
import tech.bnuuy.anigiri.feature.home.presentation.model.HomeSideEffect

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val nav = LocalNavigator.currentOrThrow
        val goToRelease = { release: Release ->
            nav.push(ScreenRegistry.get(Routes.Release(release.id)))
        }
        
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
            topBar = { AppBar() },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            HomeList(
                latestReleases = state.latestReleases,
                isLoading = state.isLoading,
                onRefresh = { vm.dispatch(HomeAction.Refresh) },
                contentPadding = innerPadding,
                error = state.error,
                onRandomClick = { vm.dispatch(HomeAction.FetchRandomRelease) },
                isRandomLoading = state.isRandomReleaseLoading,
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun AppBar() {
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
            Box(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {},
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
            Spacer(Modifier.width(gapSize))
            Box(
                Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {},
            ) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun HomeList(
        latestReleases: List<Release>,
        isLoading: Boolean,
        onRefresh: () -> Unit,
        error: Throwable? = null,
        isRandomLoading: Boolean,
        onRandomClick: () -> Unit,
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
                if (latestReleases.isNotEmpty()) {
                    item {
                        LatestReleases(latestReleases)
                    }
                }
                
                item {
                    Box(Modifier.padding(16.dp)) {
                        FetchRandomReleaseButton(
                            onClick = onRandomClick,
                            isLoading = isRandomLoading,
                        )
                    }
                }
                
                items(50) {
                    Text("Item $it",
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp))
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
    private fun LatestReleases(releases: List<Release>) {
        val nav = LocalNavigator.currentOrThrow

        Text(
            stringResource(R.string.new_episodes),
            style = Typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp),
        ) {
            items(releases) { release ->
                val releaseScreen = rememberScreen(Routes.Release(release.id))

                val relativeDate = release.latestEpisodePublishTime?.let {
                    DateUtils.getRelativeTimeSpanString(it.toEpochMilliseconds())
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable {
                            nav.push(releaseScreen)
                        }
                        .padding(8.dp)
                ) {
                    ReleasePoster(release)
                    Spacer(Modifier.height(8.dp))
                    relativeDate?.let {
                        Text(
                            it.toString(),
                            textAlign = TextAlign.Center,
                            style = Typography.labelMedium,
                        )
                    }
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
            ReleasePoster(
                release = release,
                modifier = Modifier.padding(8.dp),
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

    @Composable
    private fun ReleasePoster(release: Release, modifier: Modifier = Modifier) {
        val posterPainter = rememberAsyncImagePainter(
            model = release.posterUrl,
            contentScale = ContentScale.Crop,
        )
        val painterState by posterPainter.state.collectAsState()
        Box(modifier) {
            Box(
                Modifier
                    .width(150.dp)
                    .aspectRatio(2f / 3f)
            ) {
                when (painterState) {

                    is AsyncImagePainter.State.Error -> Icon(
                        Icons.Default.Image,
                        contentDescription = null,
                    )

                    AsyncImagePainter.State.Empty,
                    is AsyncImagePainter.State.Loading -> ShimmerLoader(
                        Modifier.fillMaxSize()
                    )

                    is AsyncImagePainter.State.Success -> Image(
                        posterPainter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .fillMaxSize(),
                    )
                }
            }
        }
    }
}
