package social.firefly.core.network.mastodon.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import social.firefly.core.network.mastodon.TrendsApi
import social.firefly.core.network.mastodon.model.responseBody.NetworkHashTag
import social.firefly.core.network.mastodon.model.responseBody.NetworkLink
import social.firefly.core.network.mastodon.model.responseBody.NetworkStatus

class TrendsApiImpl(
    private val client: HttpClient,
) : TrendsApi {

    override suspend fun getTrendingTags(
        limit: Int?,
        offset: Int?,
    ): List<NetworkHashTag> = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/trends/tags")
            parameters.apply {
                limit?.let { append("limit", limit.toString()) }
                offset?.let { append("offset", offset.toString()) }
            }
        }
    }.body()

    override suspend fun getTrendingStatuses(
        limit: Int?,
        offset: Int?,
    ): List<NetworkStatus> = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/trends/statuses")
            parameters.apply {
                limit?.let { append("limit", limit.toString()) }
                offset?.let { append("offset", offset.toString()) }
            }
        }
    }.body()

    override suspend fun getTrendingLinks(
        limit: Int?,
        offset: Int?,
    ): List<NetworkLink> = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/trends/links")
            parameters.apply {
                limit?.let { append("limit", limit.toString()) }
                offset?.let { append("offset", offset.toString()) }
            }
        }
    }.body()
}