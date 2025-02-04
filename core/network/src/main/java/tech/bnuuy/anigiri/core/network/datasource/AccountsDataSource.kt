package tech.bnuuy.anigiri.core.network.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.request.setBody
import tech.bnuuy.anigiri.core.network.datasource.request.AuthRequest
import tech.bnuuy.anigiri.core.network.datasource.response.TokenResponse
import tech.bnuuy.anigiri.core.network.resources.Accounts
import tech.bnuuy.anigiri.core.network.util.getAuthenticated

class AccountsDataSource(
    private val http: HttpClient,
) {
    suspend fun login(login: String, password: String): TokenResponse =
        http.get(Accounts.Users.Auth.Login()) {
            setBody(AuthRequest(login, password))
        }.body()
    
    suspend fun logout(): TokenResponse =
        http.getAuthenticated(Accounts.Users.Auth.Logout()).body()
}
