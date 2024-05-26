package social.firefly.core.network.mastodon.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import social.firefly.core.network.mastodon.SearchApi
import social.firefly.core.network.mastodon.model.responseBody.NetworkSearchResult

class SearchApiImpl(
    private val client: HttpClient,
) : SearchApi {

    override suspend fun search(
        query: String,
        type: String?,
        resolve: Boolean,
        accountId: String?,
        excludeUnreviewed: Boolean,
        limit: Int?,
        offset: Int?,
    ): NetworkSearchResult = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v2/search")
            parameters.apply {
                append("q", query)
                type?.let { append("type", type) }
                append("resolve", resolve.toString())
                accountId?.let { append("account_id", accountId) }
                append("exclude_unreviewed", excludeUnreviewed.toString())
                limit?.let { append("limit", limit.toString()) }
                offset?.let { append("offset", offset.toString()) }
            }
        }
    }.body()
}