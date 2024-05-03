package social.firefly.core.repository.mastodon.model

import retrofit2.HttpException
import retrofit2.Response
import social.firefly.common.parseMastodonLinkHeader
import social.firefly.core.model.paging.MastodonPagedResponse

fun <ResponseType, TransformedType> Response<List<ResponseType>>.toMastodonPagedResponse(
    transform: (ResponseType) -> TransformedType
): MastodonPagedResponse<TransformedType> {
    if (!isSuccessful) {
        throw HttpException(this)
    }

    return MastodonPagedResponse(
        items = body()?.map { transform(it) } ?: emptyList(),
        pagingLinks = headers().get("link")?.parseMastodonLinkHeader(),
    )
}