package tech.bnuuy.anigiri.core.nav

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class Routes : ScreenProvider {
    data object Home : Routes()
    data class Release(val releaseId: Int) : Routes()
    data object Search : Routes()
    data object Profile : Routes()
    data object Favorites : Routes()
    data class Player(val episodeId: String) : Routes()
    data object Collections : Routes()
}
