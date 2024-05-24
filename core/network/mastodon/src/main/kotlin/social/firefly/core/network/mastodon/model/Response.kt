package social.firefly.core.network.mastodon.model

data class Response<BODY>(
    val body: BODY,
    val code: Int,
)

fun <B> Response<B>.isSuccessful(): Boolean = code in 200..299
