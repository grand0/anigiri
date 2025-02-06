package tech.bnuuy.anigiri.core.network.cache

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.hours

class FavoritesMemoryCache {
    private val favoriteReleasesIds = mutableSetOf<Int>()
    private var updatedAt: Instant? = null
    
    fun addFavoriteReleases(releases: List<Int>) {
        favoriteReleasesIds.addAll(releases)
    }
    
    fun removeFavoriteReleases(releases: List<Int>) {
        favoriteReleasesIds.removeAll(releases)
    }
    
    fun getFavoriteReleases(): List<Int> {
        return favoriteReleasesIds.toList()
    }
    
    fun setFavoriteReleases(releases: List<Int>) {
        favoriteReleasesIds.clear()
        favoriteReleasesIds.addAll(releases)
        updatedAt = Clock.System.now()
    }
    
    fun clear() {
        favoriteReleasesIds.clear()
        updatedAt = null
    }
    
    fun isFresh(): Boolean {
        return updatedAt?.let { Clock.System.now() - it < CACHE_DURATION } == true
    }
    
    companion object {
        private val CACHE_DURATION = 1.hours
    }
}
