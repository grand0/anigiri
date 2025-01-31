package tech.bnuuy.anigiri

import android.app.Application
import cafe.adriel.voyager.core.registry.ScreenRegistry
import tech.bnuuy.anigiri.feature.home.di.homeScreenModule
import tech.bnuuy.anigiri.feature.release.di.releaseScreenModule
import tech.bnuuy.anigiri.feature.search.di.searchScreenModule

class AnigiriApp : Application() {

    override fun onCreate() {
        super.onCreate()

        ScreenRegistry {
            homeScreenModule()
            releaseScreenModule()
            searchScreenModule()
        }
    }
}
