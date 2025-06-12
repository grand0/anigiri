package tech.bnuuy.anigiri.feature.schedule.api.data.repository

import tech.bnuuy.anigiri.feature.schedule.api.data.model.Release

interface ScheduleRepository {
    suspend fun getWeekSchedule(): List<Release>
}
