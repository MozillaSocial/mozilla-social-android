package social.firefly.core.network.mastodon.utils

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import social.firefly.core.network.mastodon.model.Response

suspend inline fun <reified BODY> HttpResponse.toResponse(): Response<BODY> = Response(
    body = body(),
    code = status.value,
    headers = headers
)