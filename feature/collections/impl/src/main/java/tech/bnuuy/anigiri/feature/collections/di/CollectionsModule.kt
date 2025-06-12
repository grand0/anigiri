package tech.bnuuy.anigiri.feature.collections.di

import cafe.adriel.voyager.core.registry.screenModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import tech.bnuuy.anigiri.core.nav.Routes
import tech.bnuuy.anigiri.feature.collections.api.data.repository.CollectionsRepository
import tech.bnuuy.anigiri.feature.collections.api.usecase.GetCollectionReleasesUseCase
import tech.bnuuy.anigiri.feature.collections.data.repository.CollectionsRepositoryImpl
import tech.bnuuy.anigiri.feature.collections.presentation.CollectionsScreen
import tech.bnuuy.anigiri.feature.collections.presentation.CollectionsViewModel
import tech.bnuuy.anigiri.feature.collections.usecase.GetCollectionReleasesUseCaseImpl

val collectionsModule = module {
    factoryOf(::CollectionsRepositoryImpl) bind CollectionsRepository::class
    factoryOf(::GetCollectionReleasesUseCaseImpl) bind GetCollectionReleasesUseCase::class

    viewModelOf(::CollectionsViewModel)
}

val collectionsScreenModule = screenModule {
    register<Routes.Collections> { CollectionsScreen() }
}
