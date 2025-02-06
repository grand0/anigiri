package tech.bnuuy.anigiri.feature.search.di

import cafe.adriel.voyager.core.registry.screenModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import tech.bnuuy.anigiri.core.nav.Routes
import tech.bnuuy.anigiri.feature.search.api.data.repository.CatalogRepository
import tech.bnuuy.anigiri.feature.search.api.data.repository.SearchQueryRepository
import tech.bnuuy.anigiri.feature.search.api.usecase.AddSearchQueryUseCase
import tech.bnuuy.anigiri.feature.search.api.usecase.ClearSearchQueriesUseCase
import tech.bnuuy.anigiri.feature.search.api.usecase.FetchGenresUseCase
import tech.bnuuy.anigiri.feature.search.api.usecase.FetchYearsUseCase
import tech.bnuuy.anigiri.feature.search.api.usecase.GetAllSearchQueriesUseCase
import tech.bnuuy.anigiri.feature.search.api.usecase.SearchCatalogUseCase
import tech.bnuuy.anigiri.feature.search.data.repository.CatalogRepositoryImpl
import tech.bnuuy.anigiri.feature.search.data.repository.SearchQueryRepositoryImpl
import tech.bnuuy.anigiri.feature.search.presentation.SearchScreen
import tech.bnuuy.anigiri.feature.search.presentation.SearchViewModel
import tech.bnuuy.anigiri.feature.search.usecase.AddSearchQueryUseCaseImpl
import tech.bnuuy.anigiri.feature.search.usecase.ClearSearchQueriesUseCaseImpl
import tech.bnuuy.anigiri.feature.search.usecase.FetchGenresUseCaseImpl
import tech.bnuuy.anigiri.feature.search.usecase.FetchYearsUseCaseImpl
import tech.bnuuy.anigiri.feature.search.usecase.GetAllSearchQueriesUseCaseImpl
import tech.bnuuy.anigiri.feature.search.usecase.SearchCatalogUseCaseImpl

val searchModule = module {
    factoryOf(::CatalogRepositoryImpl) bind CatalogRepository::class
    factoryOf(::SearchQueryRepositoryImpl) bind SearchQueryRepository::class
    
    factoryOf(::SearchCatalogUseCaseImpl) bind SearchCatalogUseCase::class
    factoryOf(::FetchGenresUseCaseImpl) bind FetchGenresUseCase::class
    factoryOf(::FetchYearsUseCaseImpl) bind FetchYearsUseCase::class
    factoryOf(::AddSearchQueryUseCaseImpl) bind AddSearchQueryUseCase::class
    factoryOf(::ClearSearchQueriesUseCaseImpl) bind ClearSearchQueriesUseCase::class
    factoryOf(::GetAllSearchQueriesUseCaseImpl) bind GetAllSearchQueriesUseCase::class
    
    viewModelOf(::SearchViewModel)
}

val searchScreenModule = screenModule { 
    register<Routes.Search> { SearchScreen() }
}
