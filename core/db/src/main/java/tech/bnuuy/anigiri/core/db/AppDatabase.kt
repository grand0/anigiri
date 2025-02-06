package tech.bnuuy.anigiri.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tech.bnuuy.anigiri.core.db.converter.InstantConverter
import tech.bnuuy.anigiri.core.db.dao.SearchQueryDao
import tech.bnuuy.anigiri.core.db.entity.SearchQueryEntity

@Database(
    version = 1,
    entities = [SearchQueryEntity::class],
)
@TypeConverters(InstantConverter::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun searchQueryDao(): SearchQueryDao
}
