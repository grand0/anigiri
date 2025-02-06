package tech.bnuuy.anigiri.feature.profile.di

import cafe.adriel.voyager.core.registry.screenModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import tech.bnuuy.anigiri.core.nav.Routes
import tech.bnuuy.anigiri.feature.profile.api.data.repository.ProfileRepository
import tech.bnuuy.anigiri.feature.profile.api.usecase.GetAuthorizedProfileUseCase
import tech.bnuuy.anigiri.feature.profile.api.usecase.LoginUseCase
import tech.bnuuy.anigiri.feature.profile.api.usecase.LogoutUseCase
import tech.bnuuy.anigiri.feature.profile.data.repository.ProfileRepositoryImpl
import tech.bnuuy.anigiri.feature.profile.presentation.ProfileScreen
import tech.bnuuy.anigiri.feature.profile.presentation.ProfileViewModel
import tech.bnuuy.anigiri.feature.profile.usecase.GetAuthorizedProfileUseCaseImpl
import tech.bnuuy.anigiri.feature.profile.usecase.LoginUseCaseImpl
import tech.bnuuy.anigiri.feature.profile.usecase.LogoutUseCaseImpl

val profileModule = module {
    factory<ProfileRepository> { ProfileRepositoryImpl(get()) }
    
    factory<GetAuthorizedProfileUseCase> { GetAuthorizedProfileUseCaseImpl(get()) }
    factory<LoginUseCase> { LoginUseCaseImpl(get()) }
    factory<LogoutUseCase> { LogoutUseCaseImpl(get()) }
    
    viewModelOf(::ProfileViewModel)
}

val profileScreenModule = screenModule { 
    register<Routes.Profile> { ProfileScreen() }
}
