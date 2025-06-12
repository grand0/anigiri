package tech.bnuuy.anigiri.feature.schedule.di

import cafe.adriel.voyager.core.registry.screenModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import tech.bnuuy.anigiri.core.nav.Routes
import tech.bnuuy.anigiri.feature.schedule.api.data.repository.ScheduleRepository
import tech.bnuuy.anigiri.feature.schedule.api.usecase.GetWeekScheduleUseCase
import tech.bnuuy.anigiri.feature.schedule.data.repository.ScheduleRepositoryImpl
import tech.bnuuy.anigiri.feature.schedule.presentation.ScheduleScreen
import tech.bnuuy.anigiri.feature.schedule.presentation.ScheduleViewModel
import tech.bnuuy.anigiri.feature.schedule.usecase.GetWeekScheduleUseCaseImpl

val scheduleModule = module {
    factoryOf(::ScheduleRepositoryImpl) bind ScheduleRepository::class
    factoryOf(::GetWeekScheduleUseCaseImpl) bind GetWeekScheduleUseCase::class

    viewModelOf(::ScheduleViewModel)
}

val scheduleScreenModule = screenModule {
    register<Routes.Schedule> { ScheduleScreen() }
}
