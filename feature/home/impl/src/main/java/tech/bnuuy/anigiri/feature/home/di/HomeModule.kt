package tech.bnuuy.anigiri.feature.home.di

import cafe.adriel.voyager.core.registry.screenModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import tech.bnuuy.anigiri.core.nav.Routes
import tech.bnuuy.anigiri.feature.home.api.data.repository.ProfileRepository
import tech.bnuuy.anigiri.feature.home.api.data.repository.ReleaseRepository
import tech.bnuuy.anigiri.feature.home.api.usecase.GetFavoriteReleasesUseCase
import tech.bnuuy.anigiri.feature.home.api.usecase.GetLatestReleasesUseCase
import tech.bnuuy.anigiri.feature.home.api.usecase.GetRandomReleaseUseCase
import tech.bnuuy.anigiri.feature.home.api.usecase.GetUserAvatarUrlUseCase
import tech.bnuuy.anigiri.feature.home.data.repository.ProfileRepositoryImpl
import tech.bnuuy.anigiri.feature.home.data.repository.ReleaseRepositoryImpl
import tech.bnuuy.anigiri.feature.home.presentation.HomeViewModel
import tech.bnuuy.anigiri.feature.home.presentation.ui.HomeScreen
import tech.bnuuy.anigiri.feature.home.usecase.GetFavoriteReleasesUseCaseImpl
import tech.bnuuy.anigiri.feature.home.usecase.GetLatestReleasesUseCaseImpl
import tech.bnuuy.anigiri.feature.home.usecase.GetRandomReleaseUseCaseImpl
import tech.bnuuy.anigiri.feature.home.usecase.GetUserAvatarUrlUseCaseImpl

val homeModule = module {
//    factory<ReleaseRepository> { ReleaseRepositoryImpl(get(), get(), get()) }
    factoryOf(::ReleaseRepositoryImpl) bind ReleaseRepository::class
    factoryOf(::ProfileRepositoryImpl) bind ProfileRepository::class

//    factory<GetRandomReleaseUseCase> { GetRandomReleaseUseCaseImpl(get()) }
//    factory<GetLatestReleasesUseCase> { GetLatestReleasesUseCaseImpl(get()) }
    factoryOf(::GetRandomReleaseUseCaseImpl) bind GetRandomReleaseUseCase::class
    factoryOf(::GetLatestReleasesUseCaseImpl) bind GetLatestReleasesUseCase::class
    factoryOf(::GetFavoriteReleasesUseCaseImpl) bind GetFavoriteReleasesUseCase::class
    factoryOf(::GetUserAvatarUrlUseCaseImpl) bind GetUserAvatarUrlUseCase::class
    
    viewModelOf(::HomeViewModel)
}

val homeScreenModule = screenModule { 
    register<Routes.Home> { HomeScreen() }
}
