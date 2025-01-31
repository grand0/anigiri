package tech.bnuuy.anigiri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.util.DebugLogger
import org.koin.compose.KoinApplication
import tech.bnuuy.anigiri.core.designsystem.theme.AnigiriTheme
import tech.bnuuy.anigiri.core.designsystem.util.LocalSnackbarHostState
import tech.bnuuy.anigiri.core.network.di.networkModule
import tech.bnuuy.anigiri.feature.home.BuildConfig
import tech.bnuuy.anigiri.feature.home.di.homeModule
import tech.bnuuy.anigiri.feature.home.presentation.ui.HomeScreen
import tech.bnuuy.anigiri.feature.release.di.releaseModule
import tech.bnuuy.anigiri.feature.search.di.searchModule

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinApplication(application = {
                modules(
                    networkModule,
                    homeModule,
                    releaseModule,
                    searchModule,
                )
            }) {
                setSingletonImageLoaderFactory {
                    val builder = ImageLoader.Builder(applicationContext)
                    if (BuildConfig.DEBUG) builder.logger(DebugLogger())
                    builder.build()
                }

                val snackbarHostState = remember { SnackbarHostState() }
                CompositionLocalProvider(
                    LocalSnackbarHostState provides snackbarHostState,
                ) {
                    AnigiriTheme {
                        Navigator(
                            HomeScreen()
                        )
                    }
                }
            }
        }
    }
}
