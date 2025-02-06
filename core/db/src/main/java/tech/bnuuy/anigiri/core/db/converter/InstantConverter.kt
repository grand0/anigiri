package tech.bnuuy.anigiri.core.db.converter

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

internal class InstantConverter {
    @TypeConverter
    fun fromInstant(instant: Instant): Long {
        return instant.toEpochMilliseconds()
    }
    
    @TypeConverter
    fun toInstant(millis: Long): Instant {
        return Instant.fromEpochMilliseconds(millis)
    }
}
