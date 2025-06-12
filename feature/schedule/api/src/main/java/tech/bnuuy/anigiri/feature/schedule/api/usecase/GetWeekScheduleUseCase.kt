package tech.bnuuy.anigiri.feature.schedule.api.usecase

import tech.bnuuy.anigiri.feature.schedule.api.data.model.Release

interface GetWeekScheduleUseCase {
    suspend operator fun invoke(): Map<Int, List<Release>>
}
