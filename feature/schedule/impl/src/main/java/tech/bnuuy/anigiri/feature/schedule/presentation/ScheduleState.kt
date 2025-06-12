package tech.bnuuy.anigiri.feature.schedule.presentation

import tech.bnuuy.anigiri.feature.schedule.api.data.model.Release

data class ScheduleState(
    val schedule: Map<Int, List<Release>> = emptyMap(),
    val isLoading: Boolean = true,
    val error: Throwable? = null,

    val selectedDay: Int = 1,
)
