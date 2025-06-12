package tech.bnuuy.anigiri.feature.schedule.presentation

import tech.bnuuy.anigiri.feature.schedule.R

@Suppress("MagicNumber")
fun dayOfWeekPresentationName(day: Int): Int {
    return when (day) {
        1 -> R.string.monday
        2 -> R.string.tuesday
        3 -> R.string.wednesday
        4 -> R.string.thursday
        5 -> R.string.friday
        6 -> R.string.saturday
        7 -> R.string.sunday
        else -> throw IllegalArgumentException("Day of week must be between 1 and 7")
    }
}
