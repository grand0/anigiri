package tech.bnuuy.anigiri.feature.profile.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import tech.bnuuy.anigiri.core.designsystem.theme.Typography
import tech.bnuuy.anigiri.feature.profile.R
import tech.bnuuy.anigiri.feature.profile.api.data.model.Profile

class ProfileScreen : Screen {
    
    @Composable
    override fun Content() {
        val vm = koinViewModel<ProfileViewModel>()
        val state by vm.collectAsState()
        
        var showLoginSheet by remember { mutableStateOf(false) }
        if (state.profile != null) showLoginSheet = false

        Scaffold(
            Modifier.fillMaxSize(),
        ) { innerPadding ->
            if (state.isFirstLoad) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) { CircularProgressIndicator() }
            } else {
                ProfileComponent(
                    profile = state.profile,
                    isProfileLoading = state.isProfileLoading,
                    isLoggingOut = state.isLoggingOut,
                    onRefresh = { vm.dispatch(ProfileAction.Refresh) },
                    onLogin = { showLoginSheet = true },
                    onLogout = { vm.dispatch(ProfileAction.Logout) },
                    onRegister = { },
                    contentPadding = innerPadding,
                )
            }
        }
        
        if (showLoginSheet) {
            LoginBottomSheet(
                onLogin = { login, password -> vm.dispatch(ProfileAction.Login(login, password)) },
                isLoggingIn = state.isLoggingIn,
                error = state.loginError,
                onDismiss = { showLoginSheet = false },
            )
        }
    }
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ProfileComponent(
        profile: Profile?,
        isProfileLoading: Boolean,
        isLoggingOut: Boolean,
        onRefresh: () -> Unit,
        onLogin: () -> Unit,
        onLogout: () -> Unit,
        onRegister: () -> Unit,
        contentPadding: PaddingValues,
    ) {
        val avatarPainter = rememberAsyncImagePainter(
            model = profile?.avatarUrl,
        )
        val avatarPainterState by avatarPainter.state.collectAsState()
        val pullToRefreshState = rememberPullToRefreshState()

        PullToRefreshBox(
            isRefreshing = isProfileLoading,
            onRefresh = if (profile != null) onRefresh else { { } },
            state = pullToRefreshState,
            indicator = {
                if (profile != null) {
                    Indicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        isRefreshing = isProfileLoading,
                        state = pullToRefreshState,
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    Modifier
                        .padding(vertical = 24.dp)
                        .width(80.dp)
                        .aspectRatio(1f)
                ) {
                    if (avatarPainterState is AsyncImagePainter.State.Success) {
                        Image(
                            painter = avatarPainter,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                        )
                    }
                }
                if (profile != null) {
                    Text(
                        text = profile.login ?: stringResource(R.string.unnamed),
                        modifier = Modifier.padding(vertical = 8.dp),
                        style = Typography.titleLarge,
                    )
                    Text(
                        text = stringResource(R.string.id, profile.id),
                        modifier = Modifier.padding(vertical = 8.dp),
                        style = Typography.labelSmall,
                    )
                    Button(
                        onClick = onLogout,
                        enabled = !isLoggingOut
                    ) {
                        if (isLoggingOut) {
                            CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.AutoMirrored.Default.Logout, contentDescription = null)
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.logout))
                    }
                } else {
                    Text(
                        text = stringResource(R.string.login_to_your_account),
                        modifier = Modifier.padding(vertical = 8.dp),
                        textAlign = TextAlign.Center,
                        style = Typography.titleLarge,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Button(
                            onClick = onLogin,
                        ) {
                            Icon(Icons.AutoMirrored.Default.Login, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.login))
                        }
                        OutlinedButton(
                            onClick = onRegister,
                        ) {
                            Icon(Icons.Default.HowToReg, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.register))
                        }
                    }
                }
            }
        }
    }
}
