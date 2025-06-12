package tech.bnuuy.anigiri.feature.schedule.usecase

import tech.bnuuy.anigiri.feature.schedule.api.data.model.Release
import tech.bnuuy.anigiri.feature.schedule.api.data.repository.ScheduleRepository
import tech.bnuuy.anigiri.feature.schedule.api.usecase.GetWeekScheduleUseCase

class GetWeekScheduleUseCaseImpl(
    private val scheduleRepository: ScheduleRepository,
) : GetWeekScheduleUseCase {
    override suspend fun invoke(): Map<Int, List<Release>> {
        return scheduleRepository.getWeekSchedule().groupBy { it.publishDay }
    }
}
