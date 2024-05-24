package social.firefly.core.network.mastodon.utils

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import social.firefly.common.parseMastodonLinkHeader
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.network.mastodon.model.Response

suspend inline fun <reified ResponseType, TransformedType> HttpResponse.toMastodonPagedResponse(
    transform: (ResponseType) -> TransformedType
): MastodonPagedResponse<TransformedType> = MastodonPagedResponse(
    items = body<List<ResponseType>>().map { transform(it) },
    pagingLinks = headers["link"]?.parseMastodonLinkHeader(),
)

inline fun <reified ResponseType, TransformedType> Response<List<ResponseType>>.toMastodonPagedResponse(
    transform: (ResponseType) -> TransformedType
): MastodonPagedResponse<TransformedType> = MastodonPagedResponse(
    items = body.map { transform(it) },
    pagingLinks = headers["link"]?.parseMastodonLinkHeader(),
)