package social.firefly.core.network.mastodon.model

import io.ktor.http.Headers

data class Response<BODY>(
    val body: BODY,
    val code: Int,
    val headers: Headers,
)

fun <B> Response<B>.isSuccessful(): Boolean = code in 200..299
