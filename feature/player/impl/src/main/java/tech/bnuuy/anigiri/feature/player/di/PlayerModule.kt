package tech.bnuuy.anigiri.feature.player.di

import cafe.adriel.voyager.core.registry.screenModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import tech.bnuuy.anigiri.core.nav.Routes
import tech.bnuuy.anigiri.feature.player.api.data.repository.EpisodeRepository
import tech.bnuuy.anigiri.feature.player.api.data.repository.ProfileRepository
import tech.bnuuy.anigiri.feature.player.api.usecase.CheckAuthorizedUseCase
import tech.bnuuy.anigiri.feature.player.api.usecase.GetCommentsUseCase
import tech.bnuuy.anigiri.feature.player.api.usecase.GetEpisodeUseCase
import tech.bnuuy.anigiri.feature.player.api.usecase.SendCommentUseCase
import tech.bnuuy.anigiri.feature.player.data.repository.EpisodeRepositoryImpl
import tech.bnuuy.anigiri.feature.player.data.repository.ProfileRepositoryImpl
import tech.bnuuy.anigiri.feature.player.presentation.PlayerScreen
import tech.bnuuy.anigiri.feature.player.presentation.PlayerViewModel
import tech.bnuuy.anigiri.feature.player.usecase.CheckAuthorizedUseCaseImpl
import tech.bnuuy.anigiri.feature.player.usecase.GetCommentsUseCaseImpl
import tech.bnuuy.anigiri.feature.player.usecase.GetEpisodeUseCaseImpl
import tech.bnuuy.anigiri.feature.player.usecase.SendCommentUseCaseImpl

val playerModule = module {
    factoryOf(::EpisodeRepositoryImpl) bind EpisodeRepository::class
    factoryOf(::ProfileRepositoryImpl) bind ProfileRepository::class

    factoryOf(::GetEpisodeUseCaseImpl) bind GetEpisodeUseCase::class
    factoryOf(::GetCommentsUseCaseImpl) bind GetCommentsUseCase::class
    factoryOf(::SendCommentUseCaseImpl) bind SendCommentUseCase::class
    factoryOf(::CheckAuthorizedUseCaseImpl) bind CheckAuthorizedUseCase::class

    viewModelOf(::PlayerViewModel)
}

val playerScreenModule = screenModule {
    register<Routes.Player> { provider ->
        PlayerScreen(provider.episodeId)
    }
}
