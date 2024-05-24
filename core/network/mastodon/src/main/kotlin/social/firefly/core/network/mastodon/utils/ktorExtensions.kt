package social.firefly.core.network.mastodon.utils

import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import io.ktor.http.URLProtocol
import social.firefly.core.network.mastodon.model.Response

fun HttpRequestBuilder.path(path: String) {
    url {
        protocol = URLProtocol.HTTPS
        path(path)
    }
}

suspend inline fun <reified BODY> HttpResponse.toExternal(): Response<BODY> = Response(
    body = body(),
    code = status.value
)