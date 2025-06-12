package tech.bnuuy.anigiri.feature.schedule.data.repository

import tech.bnuuy.anigiri.core.network.datasource.AnimeDataSource
import tech.bnuuy.anigiri.feature.schedule.api.data.model.Release
import tech.bnuuy.anigiri.feature.schedule.api.data.repository.ScheduleRepository
import tech.bnuuy.anigiri.feature.schedule.data.mapper.mapList

class ScheduleRepositoryImpl(
    private val animeDataSource: AnimeDataSource,
) : ScheduleRepository {

    override suspend fun getWeekSchedule(): List<Release> {
        return animeDataSource.getWeekSchedule().mapList()
    }
}
