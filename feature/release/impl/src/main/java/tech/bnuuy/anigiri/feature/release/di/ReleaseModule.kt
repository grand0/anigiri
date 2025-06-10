package tech.bnuuy.anigiri.feature.release.di

import cafe.adriel.voyager.core.registry.screenModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import tech.bnuuy.anigiri.core.nav.Routes
import tech.bnuuy.anigiri.feature.release.api.data.repository.ProfileRepository
import tech.bnuuy.anigiri.feature.release.api.data.repository.ReleaseRepository
import tech.bnuuy.anigiri.feature.release.api.usecase.AddToFavoritesUseCase
import tech.bnuuy.anigiri.feature.release.api.usecase.CheckFavoriteReleaseUseCase
import tech.bnuuy.anigiri.feature.release.api.usecase.GetRecommendationsUseCase
import tech.bnuuy.anigiri.feature.release.api.usecase.GetReleaseUseCase
import tech.bnuuy.anigiri.feature.release.api.usecase.RemoveFromFavoritesUseCase
import tech.bnuuy.anigiri.feature.release.data.repository.ProfileRepositoryImpl
import tech.bnuuy.anigiri.feature.release.data.repository.ReleaseRepositoryImpl
import tech.bnuuy.anigiri.feature.release.presentation.ReleaseViewModel
import tech.bnuuy.anigiri.feature.release.presentation.ui.ReleaseScreen
import tech.bnuuy.anigiri.feature.release.usecase.AddToFavoritesUseCaseImpl
import tech.bnuuy.anigiri.feature.release.usecase.CheckFavoriteReleaseUseCaseImpl
import tech.bnuuy.anigiri.feature.release.usecase.GetRecommendationsUseCaseImpl
import tech.bnuuy.anigiri.feature.release.usecase.GetReleaseUseCaseImpl
import tech.bnuuy.anigiri.feature.release.usecase.RemoveFromFavoritesUseCaseImpl

val releaseModule = module {
    factoryOf(::ReleaseRepositoryImpl) bind ReleaseRepository::class
    factoryOf(::ProfileRepositoryImpl) bind ProfileRepository::class
    
    factoryOf(::GetReleaseUseCaseImpl) bind GetReleaseUseCase::class
    factoryOf(::AddToFavoritesUseCaseImpl) bind AddToFavoritesUseCase::class
    factoryOf(::CheckFavoriteReleaseUseCaseImpl) bind CheckFavoriteReleaseUseCase::class
    factoryOf(::RemoveFromFavoritesUseCaseImpl) bind RemoveFromFavoritesUseCase::class
    factoryOf(::GetRecommendationsUseCaseImpl) bind GetRecommendationsUseCase::class

    viewModelOf(::ReleaseViewModel)
}

val releaseScreenModule = screenModule { 
    register<Routes.Release> { provider ->
        ReleaseScreen(provider.releaseId)
    }
}
