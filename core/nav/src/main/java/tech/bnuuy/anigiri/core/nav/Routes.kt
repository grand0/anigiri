package tech.bnuuy.anigiri.core.nav

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class Routes : ScreenProvider {
    object Home : Routes()
    data class Release(val releaseId: Int) : Routes()
    object Search : Routes()
    object Profile : Routes()
}
