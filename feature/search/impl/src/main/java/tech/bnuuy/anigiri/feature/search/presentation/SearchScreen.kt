package tech.bnuuy.anigiri.feature.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
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
import tech.bnuuy.anigiri.feature.search.data.model.CatalogSearchUiFilter

class SearchScreen : Screen {
    
    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun Content() {
        val nav = LocalNavigator.currentOrThrow
        
        val vm = koinViewModel<SearchViewModel>()
        val state by vm.collectAsState()
        val results = vm.pagingDataFlow.collectAsLazyPagingItems()
        
        var showFilters by remember { mutableStateOf(false) }
        
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                AppBar(
                    state.filter.search,
                    onSearch = { vm.dispatch(SearchAction.Search(state.filter.copy(search = it))) },
                    onFiltersClick = { showFilters = true },
                    showFiltersBadge = !state.filter.isDefault(minYear = state.minYear, maxYear = state.maxYear),
                )
            },
        ) { innerPadding ->
            // TODO: get rid of this abomination. some stuff still breaks
            // for some reason height of the keyboard appends to the top of the innerPadding.
            // this makes the contents slide down when the keyboard slides up.
            // i don't know why this is happening, so here is a dirty fix for this.
            val correctedPadding = PaddingValues(
                top = (innerPadding.calculateTopPadding() - WindowInsets.ime.asPaddingValues().calculateBottomPadding()).coerceAtLeast(0.dp),
                bottom = innerPadding.calculateBottomPadding(),
            )

            ResultsList(
                results = results,
                onResultClick = { nav.push(ScreenRegistry.get(Routes.Release(it.id))) },
                contentPadding = correctedPadding,
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
    }
    
    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    private fun AppBar(
        query: String?,
        onSearch: (String) -> Unit,
        onFiltersClick: () -> Unit,
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
                .safeDrawingPadding()
                .padding(gapSize)
                .height(64.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            SearchTextField(
                query = query ?: "",
                onValueChange = onSearch,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .safeDrawingPadding()
                    .focusRequester(focusRequester),
            )
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
                    .clickable(onClick = onFiltersClick),
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
        LifecycleEffectOnce {
            focusRequester.requestFocus()
        }
    }
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SearchTextField(
        query: String,
        modifier: Modifier = Modifier,
        onValueChange: (String) -> Unit
    ) {
        var fieldValue by remember {
            mutableStateOf(
                TextFieldValue(
                    query,
                    selection = TextRange(query.length),
                )
            )
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
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 8.dp),
                )
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
