package tech.bnuuy.anigiri.feature.favorites.di

import cafe.adriel.voyager.core.registry.screenModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import tech.bnuuy.anigiri.core.nav.Routes
import tech.bnuuy.anigiri.feature.favorites.api.data.repository.FavoritesRepository
import tech.bnuuy.anigiri.feature.favorites.api.usecase.FetchFavoriteReleasesUseCase
import tech.bnuuy.anigiri.feature.favorites.data.repository.FavoritesRepositoryImpl
import tech.bnuuy.anigiri.feature.favorites.presentation.FavoritesScreen
import tech.bnuuy.anigiri.feature.favorites.presentation.FavoritesViewModel
import tech.bnuuy.anigiri.feature.favorites.usecase.FetchFavoriteReleasesUseCaseImpl

val favoritesModule = module {
    factoryOf(::FavoritesRepositoryImpl) bind FavoritesRepository::class
    
    factoryOf(::FetchFavoriteReleasesUseCaseImpl) bind FetchFavoriteReleasesUseCase::class
    
    viewModelOf(::FavoritesViewModel)
}

val favoritesScreenModule = screenModule { 
    register<Routes.Favorites> { FavoritesScreen() }
}
