package tech.bnuuy.anigiri.feature.release.di

import cafe.adriel.voyager.core.registry.screenModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import tech.bnuuy.anigiri.core.nav.Routes
import tech.bnuuy.anigiri.feature.release.api.data.repository.ReleaseRepository
import tech.bnuuy.anigiri.feature.release.api.usecase.GetReleaseUseCase
import tech.bnuuy.anigiri.feature.release.data.repository.ReleaseRepositoryImpl
import tech.bnuuy.anigiri.feature.release.presentation.ReleaseViewModel
import tech.bnuuy.anigiri.feature.release.presentation.ui.ReleaseScreen
import tech.bnuuy.anigiri.feature.release.usecase.GetReleaseUseCaseImpl

val releaseModule = module {
    factory<ReleaseRepository> { ReleaseRepositoryImpl(get()) }
    
    factory<GetReleaseUseCase> { GetReleaseUseCaseImpl(get()) }

    viewModelOf(::ReleaseViewModel)
}

val releaseScreenModule = screenModule { 
    register<Routes.Release> { provider ->
        ReleaseScreen(provider.releaseId)
    }
}
