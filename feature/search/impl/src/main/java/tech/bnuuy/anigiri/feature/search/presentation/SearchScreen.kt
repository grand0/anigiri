package tech.bnuuy.anigiri.feature.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
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
import tech.bnuuy.anigiri.feature.search.api.data.model.Release

class SearchScreen : Screen {
    
    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun Content() {
        val nav = LocalNavigator.currentOrThrow
        
        val vm = koinViewModel<SearchViewModel>()
        val state by vm.collectAsState()
        val results = vm.pagingDataFlow.collectAsLazyPagingItems()
        
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                AppBar(
                    state.query,
                    onSearch = { vm.dispatch(SearchAction.Search(it)) },
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
            )
        }
    }
    
    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    private fun AppBar(
        query: String,
        onSearch: (String) -> Unit,
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
            Box(
                Modifier
                    .fillMaxHeight()
                    .background(
                        MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Row(
                    Modifier
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                    )
                    Spacer(Modifier.width(16.dp))
                    SearchTextField(
                        query,
                        onValueChange = onSearch,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .safeDrawingPadding()
                            .focusRequester(focusRequester),
                        singleLine = true,
                    )
                }
            }
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
    
    // TODO: rewrite to standard TextField
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun SearchTextField(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        singleLine: Boolean = false,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        textStyle: TextStyle = LocalTextStyle.current,
        colors: TextFieldColors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        )
    ) {
        var textFieldValueState by remember {
            mutableStateOf(
                TextFieldValue(
                    text = value,
                    selection = TextRange(value.length)
                )
            )
        }
        val interactionSource = remember { MutableInteractionSource() }
        val textColor =
            textStyle.color.takeOrElse {
                val focused = interactionSource.collectIsFocusedAsState().value
                when {
                    !enabled -> colors.disabledTextColor
                    focused -> colors.focusedTextColor
                    else -> colors.unfocusedTextColor
                }
            }
        val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))
        BasicTextField(
            value = textFieldValueState,
            onValueChange = {
                textFieldValueState = it
                onValueChange(it.text)
            },
            modifier = modifier,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            enabled = enabled,
            singleLine = singleLine,
            textStyle = mergedTextStyle,
            cursorBrush = SolidColor(colors.cursorColor),
        ) { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = value,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                singleLine = singleLine,
                enabled = enabled,
                interactionSource = interactionSource,
                colors = colors,
                contentPadding = PaddingValues(0.dp),
            )
        }
    }
    
    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun ResultsList(
        results: LazyPagingItems<Release>,
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
                .imeNestedScroll()
                .imePadding(),
            contentPadding = contentPadding,
        ) {
            item {
                if (results.loadState.prepend is LoadState.Loading) {
                    CircularProgressIndicator()
                } else if (results.loadState.prepend is LoadState.Error) {
                    Text((results.loadState.prepend as LoadState.Error).error.toString())
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
                        modifier = Modifier.clickable { onResultClick(release) },
                    )
                }
            }
            
            item {
                if (results.loadState.append is LoadState.Loading) {
                    CircularProgressIndicator()
                } else if (results.loadState.append is LoadState.Error) {
                    Text((results.loadState.append as LoadState.Error).error.toString())
                }
            }
        }
    }
}
