package tech.bnuuy.anigiri.feature.collections.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import tech.bnuuy.anigiri.core.designsystem.component.PosterListItem
import tech.bnuuy.anigiri.core.designsystem.theme.Typography
import tech.bnuuy.anigiri.core.nav.Routes
import tech.bnuuy.anigiri.core.network.datasource.enumeration.CollectionType
import tech.bnuuy.anigiri.feature.collections.R
import tech.bnuuy.anigiri.feature.collections.api.data.model.Release
import tech.bnuuy.anigiri.feature.collections.presentation.mapper.icon
import tech.bnuuy.anigiri.feature.collections.presentation.mapper.presentationName

class CollectionsScreen : Screen {

    @Composable
    override fun Content() {
        val nav = LocalNavigator.currentOrThrow

        val vm = koinViewModel<CollectionsViewModel>()
        val state by vm.collectAsState()
        val collectionReleases = vm.pagingDataFlow.collectAsLazyPagingItems()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { AppBar(
                onBackPressed = { nav.pop() },
                selectedType = state.collectionType,
                onTypeSelected = { vm.dispatch(CollectionsAction.FetchCollection(it)) }
            ) }
        ) { innerPadding ->
            CollectionReleasesList(
                releases = collectionReleases,
                onReleaseClick = { nav.push(ScreenRegistry.get(Routes.Release(it.id))) },
                contentPadding = innerPadding,
                totalItems = state.totalItems,
            )
        }
    }

    @Composable
    private fun AppBar(
        onBackPressed: () -> Unit,
        selectedType: CollectionType,
        onTypeSelected: (CollectionType) -> Unit,
    ) {
        val gradient = Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.surface.copy(alpha = 1.0f),
                MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
                MaterialTheme.colorScheme.surface.copy(alpha = 0.0f),
            )
        )

        Column(
            Modifier
                .background(gradient)
                .statusBarsPadding()
                .fillMaxWidth()
        ) {
            Box(
                Modifier
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

                Text(
                    stringResource(R.string.collections),
                    modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    style = Typography.titleLarge,
                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            ) {
                Box(Modifier.width(8.dp))
                CollectionType.entries.forEach { type ->
                    FilterChip(
                        onClick = { onTypeSelected(type) },
                        label = { Text(stringResource(type.presentationName())) },
                        selected = selectedType == type,
                        leadingIcon = {
                            Icon(
                                imageVector = type.icon(selected = selectedType == type),
                                contentDescription = null,
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        )
                    )
                }
                Box(Modifier.width(8.dp))
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CollectionReleasesList(
        releases: LazyPagingItems<Release>,
        onReleaseClick: (Release) -> Unit,
        contentPadding: PaddingValues,
        totalItems: Int,
    ) {
        if (releases.loadState.refresh is LoadState.Error) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    (releases.loadState.refresh as LoadState.Error).error.toString(),
                    textAlign = TextAlign.Center
                )
            }
            return
        }

        val pullToRefreshState = rememberPullToRefreshState()
        val isRefreshing = releases.loadState.refresh is LoadState.Loading

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { releases.refresh() },
            state = pullToRefreshState,
            indicator = {
                Indicator(
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    modifier = Modifier.align(Alignment.TopCenter),
                    threshold = 220.dp
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding(),
                contentPadding = contentPadding,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Text(
                        stringResource(R.string.n_collection_releases, totalItems),
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                }

                item {
                    if (releases.loadState.prepend is LoadState.Loading) {
                        CircularProgressIndicator(Modifier.padding(vertical = 16.dp))
                    }
                }

                items(
                    releases.itemCount,
                    key = releases.itemKey { it.id },
                ) { index ->
                    releases[index]?.let { release ->
                        val posterPainter = rememberAsyncImagePainter(
                            model = release.posterUrl,
                        )
                        val painterState by posterPainter.state.collectAsState()

                        PosterListItem(
                            title = release.name,
                            description = release.description,
                            painter = posterPainter,
                            isImageLoading = painterState is AsyncImagePainter.State.Loading,
                            isImageError = painterState is AsyncImagePainter.State.Error,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onReleaseClick(release) },
                        )
                    }
                }

                item {
                    if (releases.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator(Modifier.padding(vertical = 16.dp))
                    }
                }
            }
        }
    }
}
