package tech.bnuuy.anigiri.feature.schedule.presentation

sealed interface ScheduleAction {
    data object FetchSchedule : ScheduleAction
    data class SelectDay(val day: Int) : ScheduleAction
}
