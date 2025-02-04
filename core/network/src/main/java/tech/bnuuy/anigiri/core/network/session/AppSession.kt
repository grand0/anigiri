package tech.bnuuy.anigiri.core.network.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import tech.bnuuy.anigiri.core.network.datasource.AccountsDataSource
import tech.bnuuy.anigiri.core.network.util.Keys.ANIGIRI_AUTH_TOKEN_KEY

class AppSession(
    private val dataStore: DataStore<Preferences>,
    private val accountsDataSource: AccountsDataSource,
) {
    suspend fun authorize(login: String, password: String) {
        accountsDataSource.login(login, password).token?.let { token ->
            dataStore.edit { prefs ->
                prefs[stringPreferencesKey(ANIGIRI_AUTH_TOKEN_KEY)] = token
            }
        }
    }
    
    suspend fun getAuthToken(): String? {
        return dataStore.data.first()[stringPreferencesKey(ANIGIRI_AUTH_TOKEN_KEY)]
    }
}
