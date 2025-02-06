package tech.bnuuy.anigiri.core.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "queries",
    indices = [Index(value = ["query"], unique = true)],
)
data class SearchQueryEntity(
    @PrimaryKey val query: String,
    @ColumnInfo(name = "timestamp") val timestamp: Instant,
)
