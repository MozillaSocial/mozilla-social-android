package social.firefly.core.network.mastodon.model

import io.ktor.http.Headers

data class Response<BODY>(
    val body: BODY,
    val code: Int,
    val headers: Headers,
)
