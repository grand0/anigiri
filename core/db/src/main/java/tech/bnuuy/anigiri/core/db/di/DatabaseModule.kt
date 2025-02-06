package tech.bnuuy.anigiri.core.db.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import tech.bnuuy.anigiri.core.db.AppDatabase
import tech.bnuuy.anigiri.core.db.dao.SearchQueryDao
import tech.bnuuy.anigiri.core.db.datasource.SearchQueryDataSource

val databaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "anigiri_db"
        ).build()
    }
    
    factory<SearchQueryDao> { get<AppDatabase>().searchQueryDao() }
    
    factoryOf(::SearchQueryDataSource)
}
