package tech.bnuuy.anigiri.feature.schedule.presentation

import androidx.lifecycle.ViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.todayIn
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import tech.bnuuy.anigiri.feature.schedule.api.usecase.GetWeekScheduleUseCase

class ScheduleViewModel(
    private val getWeekScheduleUseCase: GetWeekScheduleUseCase,
) : ContainerHost<ScheduleState, Nothing>, ViewModel() {
    override val container = container<ScheduleState, Nothing>(ScheduleState(
        selectedDay = today(),
    )) {
        fetchSchedule()
    }

    fun dispatch(action: ScheduleAction) {
        when (action) {
            ScheduleAction.FetchSchedule -> fetchSchedule()
            is ScheduleAction.SelectDay -> selectDay(action.day)
        }
    }

    private fun selectDay(day: Int) = intent {
        reduce { state.copy(selectedDay = day) }
    }

    private fun fetchSchedule() = intent {
        reduce { state.copy(
            isLoading = true,
            error = null,
        ) }
        val result = runCatching { getWeekScheduleUseCase() }
        reduce { state.copy(
            schedule = result.getOrDefault(state.schedule),
            isLoading = false,
            error = result.exceptionOrNull(),
        ) }
    }

    private fun today() =
        Clock.System.todayIn(TimeZone.currentSystemDefault()).dayOfWeek.isoDayNumber
}
