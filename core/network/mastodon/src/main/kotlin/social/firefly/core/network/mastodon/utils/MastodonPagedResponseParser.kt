package social.firefly.core.network.mastodon.utils

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import social.firefly.common.parseMastodonLinkHeader
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.network.mastodon.exceptions.HttpException

suspend inline fun <reified ResponseType, TransformedType> HttpResponse.toMastodonPagedResponse(
    transform: (ResponseType) -> TransformedType
): MastodonPagedResponse<TransformedType> {
    val isSuccessful = status.value in 200..299
    if (!isSuccessful) {
        throw HttpException(body())
    }

    return MastodonPagedResponse(
        items = body<List<ResponseType>>().map { transform(it) },
        pagingLinks = headers["link"]?.parseMastodonLinkHeader(),
    )
}