package tech.bnuuy.anigiri.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ANIGIRI_DATASTORE")

val appModule = module {
    factory<DataStore<Preferences>> {
        androidApplication().dataStore
    }
}
