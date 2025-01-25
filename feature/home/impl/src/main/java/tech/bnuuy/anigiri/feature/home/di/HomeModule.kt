package tech.bnuuy.anigiri.feature.home.di

import cafe.adriel.voyager.core.registry.screenModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import tech.bnuuy.anigiri.core.nav.Routes
import tech.bnuuy.anigiri.feature.home.api.data.repository.ReleaseRepository
import tech.bnuuy.anigiri.feature.home.api.usecase.GetLatestReleasesUseCase
import tech.bnuuy.anigiri.feature.home.api.usecase.GetRandomReleaseUseCase
import tech.bnuuy.anigiri.feature.home.data.repository.ReleaseRepositoryImpl
import tech.bnuuy.anigiri.feature.home.presentation.HomeViewModel
import tech.bnuuy.anigiri.feature.home.presentation.ui.HomeScreen
import tech.bnuuy.anigiri.feature.home.usecase.GetLatestReleasesUseCaseImpl
import tech.bnuuy.anigiri.feature.home.usecase.GetRandomReleaseUseCaseImpl

val homeModule = module {
    factory<ReleaseRepository> { ReleaseRepositoryImpl(get()) }

    factory<GetRandomReleaseUseCase> { GetRandomReleaseUseCaseImpl(get()) }
    factory<GetLatestReleasesUseCase> { GetLatestReleasesUseCaseImpl(get()) }
    
    viewModelOf(::HomeViewModel)
}

val homeScreenModule = screenModule { 
    register<Routes.Home> { HomeScreen() }
}
