package tech.bnuuy.anigiri.feature.release.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState
import tech.bnuuy.anigiri.core.designsystem.component.ExpandableCard
import tech.bnuuy.anigiri.core.designsystem.component.Poster
import tech.bnuuy.anigiri.core.designsystem.component.ShimmerLoader
import tech.bnuuy.anigiri.core.designsystem.theme.Typography
import tech.bnuuy.anigiri.feature.release.api.data.model.Release
import tech.bnuuy.anigiri.feature.release.R
import tech.bnuuy.anigiri.feature.release.presentation.ReleaseViewModel

class ReleaseScreen(val releaseId: Int) : Screen {
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val nav = LocalNavigator.currentOrThrow
        
        val vm = koinViewModel<ReleaseViewModel> { parametersOf(releaseId) }
        val state by vm.collectAsState()
        
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        ) { innerPadding ->
            Box(
                Modifier
                    .safeDrawingPadding()
                    .padding(16.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.1f))
                    .clickable { nav.pop() }
                    .zIndex(Float.MAX_VALUE)
            ) {
                Box(
                    Modifier.padding(12.dp),
                ) {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null,
                    )
                }
            }
            if (state.isLoading) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            } else if (state.error != null || state.release == null) {
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
                val release = state.release!!
                ReleaseInfo(release, contentPadding = innerPadding)
            }
        }
    }
    
    @Composable
    private fun ReleaseInfo(release: Release, contentPadding: PaddingValues) {
        val posterPainter = rememberAsyncImagePainter(
            model = release.posterUrl,
            contentScale = ContentScale.Crop,
        )
        val painterState by posterPainter.state.collectAsState()
        
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = contentPadding,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Poster(
                    painter = posterPainter,
                    isLoading = painterState is AsyncImagePainter.State.Loading,
                    isError = painterState is AsyncImagePainter.State.Error,
                    modifier = Modifier
                        .width(250.dp)
                        .padding(vertical = 32.dp),
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

    @Composable
    private fun ReleasePoster(release: Release, modifier: Modifier = Modifier) {
        val posterPainter = rememberAsyncImagePainter(
            model = release.posterUrl,
            contentScale = ContentScale.Crop,
        )
        val painterState by posterPainter.state.collectAsState()
        Box(
            modifier
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
