package tech.bnuuy.anigiri.feature.release.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState
import tech.bnuuy.anigiri.core.designsystem.component.ExpandableCard
import tech.bnuuy.anigiri.core.designsystem.component.Poster
import tech.bnuuy.anigiri.core.designsystem.theme.Typography
import tech.bnuuy.anigiri.core.designsystem.util.LocalSnackbarHostState
import tech.bnuuy.anigiri.feature.release.R
import tech.bnuuy.anigiri.feature.release.api.data.model.Release
import tech.bnuuy.anigiri.feature.release.presentation.ReleaseViewModel
import tech.bnuuy.anigiri.feature.release.presentation.model.ReleaseAction

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
                                if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
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

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
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
                                .padding(bottom = 32.dp),
                        )
                    }
                    item {
                        Text(
                            release.name,
                            style = Typography.titleLarge,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                    item {
                        Box(Modifier.padding(horizontal = 12.dp)) {
                            ExpandableCard(
                                release.description,
                                title = stringResource(R.string.description),
                                textAlign = TextAlign.Justify,
                                modifier = Modifier.padding(8.dp),
                                titleModifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}
