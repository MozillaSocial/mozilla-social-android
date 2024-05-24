package social.firefly.core.repository.mastodon.model.block

import retrofit2.Response
import social.firefly.common.parseMastodonLinkHeader
import social.firefly.core.network.mastodon.model.responseBody.NetworkAccount
import social.firefly.core.repository.mastodon.model.status.toExternalModel

fun Response<List<social.firefly.core.network.mastodon.model.responseBody.NetworkAccount>>.toAccountsList() =
    body()?.map { it.toExternalModel() } ?: emptyList()

fun Response<List<social.firefly.core.network.mastodon.model.responseBody.NetworkAccount>>.toPagingLinks() =
    headers().get("link")?.parseMastodonLinkHeader()
