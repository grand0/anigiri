package tech.bnuuy.anigiri.feature.search.di

import cafe.adriel.voyager.core.registry.screenModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import tech.bnuuy.anigiri.core.nav.Routes
import tech.bnuuy.anigiri.feature.search.api.data.repository.CatalogRepository
import tech.bnuuy.anigiri.feature.search.api.usecase.FetchGenresUseCase
import tech.bnuuy.anigiri.feature.search.api.usecase.FetchYearsUseCase
import tech.bnuuy.anigiri.feature.search.api.usecase.SearchCatalogUseCase
import tech.bnuuy.anigiri.feature.search.data.repository.CatalogRepositoryImpl
import tech.bnuuy.anigiri.feature.search.presentation.SearchScreen
import tech.bnuuy.anigiri.feature.search.presentation.SearchViewModel
import tech.bnuuy.anigiri.feature.search.usecase.FetchGenresUseCaseImpl
import tech.bnuuy.anigiri.feature.search.usecase.FetchYearsUseCaseImpl
import tech.bnuuy.anigiri.feature.search.usecase.SearchCatalogUseCaseImpl

val searchModule = module {
    factory<CatalogRepository> { CatalogRepositoryImpl(get()) }
    
    factory<SearchCatalogUseCase> { SearchCatalogUseCaseImpl(get()) }
    factory<FetchGenresUseCase> { FetchGenresUseCaseImpl(get()) }
    factory<FetchYearsUseCase> { FetchYearsUseCaseImpl(get()) }
    
    viewModelOf(::SearchViewModel)
}

val searchScreenModule = screenModule { 
    register<Routes.Search> { SearchScreen() }
}
