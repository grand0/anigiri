package tech.bnuuy.anigiri.core.network.util

import com.google.firebase.firestore.DocumentSnapshot
import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.delete
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
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

internal suspend inline fun <reified T : Any> HttpClient.postAuthenticated(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    val appSession = NetworkComponent.appSession
    return post(resource) {
        val token = appSession.getAuthToken()
        if (token != null) {
            header("Authorization", "Bearer $token")
        }
        builder()
    }
}

internal suspend inline fun <reified T : Any> HttpClient.deleteAuthenticated(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    val appSession = NetworkComponent.appSession
    return delete(resource) {
        val token = appSession.getAuthToken()
        if (token != null) {
            header("Authorization", "Bearer $token")
        }
        builder()
    }
}

internal fun DocumentSnapshot.getInstant(field: String): Instant? =
    getTimestamp(field)?.toInstant()?.toKotlinInstant()
