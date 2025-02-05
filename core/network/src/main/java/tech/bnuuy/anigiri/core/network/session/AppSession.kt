package tech.bnuuy.anigiri.core.network.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import tech.bnuuy.anigiri.core.network.datasource.AccountsDataSource
import tech.bnuuy.anigiri.core.network.datasource.response.ProfileResponse
import tech.bnuuy.anigiri.core.network.util.Keys.ANIGIRI_AUTH_TOKEN_KEY

class AppSession(
    private val dataStore: DataStore<Preferences>,
    private val accountsDataSource: AccountsDataSource,
) {
    private var cachedProfileResponse: ProfileResponse? = null
    
    suspend fun authorize(login: String, password: String): ProfileResponse {
        accountsDataSource.login(login, password).token!!.let { token ->
            dataStore.edit { prefs ->
                prefs[stringPreferencesKey(ANIGIRI_AUTH_TOKEN_KEY)] = token
            }
            return fetchAuthorizedUser()!!
        }
    }
    
    suspend fun logout() {
        accountsDataSource.logout().let {
            dataStore.edit { prefs ->
                prefs.remove(stringPreferencesKey(ANIGIRI_AUTH_TOKEN_KEY))
            }
            cachedProfileResponse = null
        }
    }
    
    suspend fun getAuthorizedUser(useCache: Boolean = true): ProfileResponse? {
        if (useCache && cachedProfileResponse != null) return cachedProfileResponse!!
        return fetchAuthorizedUser()
    }
    
    private suspend fun fetchAuthorizedUser(): ProfileResponse? {
        if (getAuthToken() == null) return null
        
        return accountsDataSource.myProfile().also {
            cachedProfileResponse = it
        }
    }
    
    suspend fun getAuthToken(): String? {
        return dataStore.data.first()[stringPreferencesKey(ANIGIRI_AUTH_TOKEN_KEY)]
    }
}
