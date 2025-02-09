package tech.bnuuy.anigiri.feature.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.LifecycleEffectOnce
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import tech.bnuuy.anigiri.core.designsystem.component.PosterListItem
import tech.bnuuy.anigiri.core.nav.Routes
import tech.bnuuy.anigiri.feature.search.R
import tech.bnuuy.anigiri.feature.search.api.data.model.Release

class SearchScreen : Screen {
    
    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun Content() {
        val nav = LocalNavigator.currentOrThrow
        
        val vm = koinViewModel<SearchViewModel>()
        val state by vm.collectAsState()
        val results = vm.pagingDataFlow.collectAsLazyPagingItems()
        
        var showFilters by remember { mutableStateOf(false) }
        var showSearchHistory by remember { mutableStateOf(false) }
        
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                AppBar(
                    state.filter.search,
                    onSearch = { vm.dispatch(SearchAction.Search(state.filter.copy(search = it))) },
                    onFiltersClick = { showFilters = true },
                    showFiltersBadge = !state.filter.isDefault(minYear = state.minYear, maxYear = state.maxYear),
                    onSearchHistoryClick = { showSearchHistory = true },
                    onBackButtonClick = { nav.pop() }
                )
            },
        ) { innerPadding ->
            ResultsList(
                results = results,
                onResultClick = { nav.push(ScreenRegistry.get(Routes.Release(it.id))) },
                contentPadding = innerPadding,
                totalItems = state.totalItems,
            )
        }
        
        if (showFilters) {
            FiltersBottomSheet(
                initialFilter = state.filter,
                onDismiss = {
                    showFilters = false
                    vm.dispatch(SearchAction.Search(it))
                },
                allGenres = state.genres,
                minYear = state.minYear,
                maxYear = state.maxYear,
                isLoading = state.filtersLoading,
            )
        }
        
        if (showSearchHistory) {
            SearchHistoryBottomSheet(
                queries = state.searchHistory,
                onDismiss = { showSearchHistory = false },
                onQueryClick = { searchQuery ->
                    showSearchHistory = false
                    vm.dispatch(SearchAction.Search(state.filter.copy(search = searchQuery.query)))
                },
                onClearClick = { vm.dispatch(SearchAction.ClearSearchHistory) }
            )
        }
    }
    
    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    private fun AppBar(
        query: String?,
        onSearch: (String) -> Unit,
        onFiltersClick: () -> Unit,
        onSearchHistoryClick: () -> Unit,
        onBackButtonClick: () -> Unit,
        showFiltersBadge: Boolean = false,
    ) {
        val gradient = Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.surface.copy(alpha = 1.0f),
                MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
                MaterialTheme.colorScheme.surface.copy(alpha = 0.0f),
            )
        )
        val gapSize = 12.dp
        val focusRequester = remember { FocusRequester() }
        
        Row(
            Modifier
                .background(gradient)
                .statusBarsPadding()
                .padding(gapSize)
                .height(64.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            SearchTextField(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .focusRequester(focusRequester),
                query = query ?: "",
                onValueChange = onSearch,
                onSearchHistoryClick = onSearchHistoryClick,
                onBackButtonClick = onBackButtonClick,
            )
            Spacer(Modifier.width(gapSize))
            FiltersButton(onClick = onFiltersClick, showFiltersBadge = showFiltersBadge)
        }
        LifecycleEffectOnce {
            focusRequester.requestFocus()
        }
    }
    
    @Suppress("LongMethod")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SearchTextField(
        query: String,
        modifier: Modifier = Modifier,
        onValueChange: (String) -> Unit,
        onSearchHistoryClick: () -> Unit,
        onBackButtonClick: () -> Unit,
    ) {
        var fieldValue by remember {
            mutableStateOf(
                TextFieldValue(
                    query,
                    selection = TextRange(query.length),
                )
            )
        }

        LaunchedEffect(query) {
            if (fieldValue.text != query) {
                fieldValue = TextFieldValue(
                    query,
                    selection = TextRange(query.length),
                )
            }
        }

        TextField(
            value = fieldValue,
            onValueChange = {
                fieldValue = it
                onValueChange(it.text)
            },
            modifier = modifier
                .fillMaxWidth(),
            leadingIcon = {
                IconButton(
                    onClick = onBackButtonClick,
                    modifier = Modifier.padding(start = 4.dp),
                ) {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null,
                    )
                }
            },
            trailingIcon = {
                if (fieldValue.text.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            fieldValue = TextFieldValue("")
                            onValueChange("")
                        },
                        modifier = Modifier.padding(end = 8.dp),
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null,
                        )
                    }
                } else {
                    IconButton(
                        onClick = { onSearchHistoryClick() },
                        modifier = Modifier.padding(end = 8.dp),
                    ) {
                        Icon(
                            Icons.Default.History,
                            contentDescription = null,
                        )
                    }
                }
            },
            placeholder = {
                Text(stringResource(R.string.search))
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                errorContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        )
    }
    
    @Composable
    private fun FiltersButton(
        onClick: () -> Unit = {},
        showFiltersBadge: Boolean = false,
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
        ) {
            BadgedBox(
                modifier = Modifier.align(Alignment.Center),
                badge = {
                    if (showFiltersBadge) {
                        Badge()
                    }
                },
            ) {
                Icon(
                    Icons.Default.FilterList,
                    contentDescription = null,
                )
            }
        }
    }
    
    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun ResultsList(
        results: LazyPagingItems<Release>,
        totalItems: Int,
        onResultClick: (Release) -> Unit,
        contentPadding: PaddingValues,
    ) {
        if (results.loadState.refresh is LoadState.Loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        } else if (results.loadState.refresh is LoadState.Error) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    (results.loadState.refresh as LoadState.Error).error.toString(),
                    textAlign = TextAlign.Center,
                )
            }
            return
        }
        
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            contentPadding = contentPadding,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Text(
                    stringResource(R.string.found_n_releases, totalItems),
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
            
            item {
                if (results.loadState.prepend is LoadState.Loading) {
                    CircularProgressIndicator(Modifier.padding(vertical = 16.dp))
                }
            }
            
            items(
                results.itemCount,
                key = results.itemKey { it.id },
            ) { index ->
                results[index]?.let { release ->
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
                        modifier = Modifier.fillMaxWidth().clickable { onResultClick(release) },
                    )
                }
            }
            
            item {
                if (results.loadState.append is LoadState.Loading) {
                    CircularProgressIndicator(Modifier.padding(vertical = 16.dp))
                }
            }
        }
    }
}
