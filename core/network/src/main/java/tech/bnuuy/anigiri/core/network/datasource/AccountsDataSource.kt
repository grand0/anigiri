package tech.bnuuy.anigiri.core.network.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import tech.bnuuy.anigiri.core.network.datasource.request.AuthRequest
import tech.bnuuy.anigiri.core.network.datasource.response.ProfileResponse
import tech.bnuuy.anigiri.core.network.datasource.response.TokenResponse
import tech.bnuuy.anigiri.core.network.resources.Accounts
import tech.bnuuy.anigiri.core.network.session.AppSession
import tech.bnuuy.anigiri.core.network.util.getAuthenticated
import tech.bnuuy.anigiri.core.network.util.postAuthenticated

class AccountsDataSource(
    private val http: HttpClient,
) {
    suspend fun login(login: String, password: String): TokenResponse =
        http.post(Accounts.Users.Auth.Login()) {
            contentType(ContentType.Application.Json)
            setBody(AuthRequest(login, password))
        }.body()
    
    suspend fun logout(): TokenResponse =
        http.postAuthenticated(Accounts.Users.Auth.Logout()).body()
    
    suspend fun myProfile(): ProfileResponse =
        http.getAuthenticated(Accounts.Users.Me.Profile()).body()
}
