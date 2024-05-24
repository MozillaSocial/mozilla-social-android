package social.firefly.core.network.mastodon.utils

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import social.firefly.common.parseMastodonLinkHeader
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.network.mastodon.exceptions.HttpException
import social.firefly.core.network.mastodon.model.Response
import social.firefly.core.network.mastodon.model.isSuccessful

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

suspend inline fun <reified ResponseType, TransformedType> Response<List<ResponseType>>.toMastodonPagedResponse(
    transform: (ResponseType) -> TransformedType
): MastodonPagedResponse<TransformedType> {
    if (!isSuccessful()) {
        throw HttpException(body.toString())
    }

    return MastodonPagedResponse(
        items = body.map { transform(it) },
        pagingLinks = headers["link"]?.parseMastodonLinkHeader(),
    )
}