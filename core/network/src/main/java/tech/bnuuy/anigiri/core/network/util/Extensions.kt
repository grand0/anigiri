package tech.bnuuy.anigiri.core.network.util

import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import tech.bnuuy.anigiri.core.network.di.NetworkComponent

internal suspend inline fun <reified T : Any> HttpClient.getAuthenticated(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    val appSession = NetworkComponent.appSession
    return get(resource) {
        val token = appSession.getAuthToken()
        if (token != null) {
            header("Authorization", "Bearer $token")
        }
        builder()
    }
}
