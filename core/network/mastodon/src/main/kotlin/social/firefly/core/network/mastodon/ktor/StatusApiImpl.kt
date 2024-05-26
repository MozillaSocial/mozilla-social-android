package social.firefly.core.network.mastodon.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import social.firefly.core.network.mastodon.StatusApi
import social.firefly.core.network.mastodon.model.request.NetworkPollVote
import social.firefly.core.network.mastodon.model.request.NetworkStatusCreate
import social.firefly.core.network.mastodon.model.responseBody.NetworkContext
import social.firefly.core.network.mastodon.model.responseBody.NetworkPoll
import social.firefly.core.network.mastodon.model.responseBody.NetworkStatus

class StatusApiImpl(
    private val client: HttpClient,
) : StatusApi {

    override suspend fun getStatus(statusId: String): NetworkStatus = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/statuses/$statusId")
        }
    }.body()

    override suspend fun postStatus(status: NetworkStatusCreate): NetworkStatus = client.post {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/statuses")
        }
        contentType(ContentType.Application.Json)
        setBody(status)
    }.body()

    override suspend fun editStatus(
        statusId: String,
        status: NetworkStatusCreate
    ): NetworkStatus = client.put {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/statuses/$statusId")
        }
        contentType(ContentType.Application.Json)
        setBody(status)
    }.body()

    override suspend fun voteOnPoll(
        pollId: String,
        body: NetworkPollVote
    ): NetworkPoll = client.post {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/polls/$pollId/votes")
        }
        contentType(ContentType.Application.Json)
        setBody(body)
    }.body()

    override suspend fun boostStatus(statusId: String): NetworkStatus = client.post {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/statuses/$statusId/reblog")
        }
    }.body()

    override suspend fun unBoostStatus(statusId: String): NetworkStatus = client.post {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/statuses/$statusId/unreblog")
        }
    }.body()

    override suspend fun favoriteStatus(statusId: String): NetworkStatus = client.post {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/statuses/$statusId/favourite")
        }
    }.body()

    override suspend fun unFavoriteStatus(statusId: String): NetworkStatus = client.post {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/statuses/$statusId/unfavourite")
        }
    }.body()

    override suspend fun getStatusContext(statusId: String): NetworkContext = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/statuses/$statusId/context")
        }
    }.body()

    override suspend fun deleteStatus(statusId: String) {
        client.delete {
            url {
                protocol = URLProtocol.HTTPS
                path("api/v1/statuses/$statusId")
            }
        }
    }

    override suspend fun bookmarkStatus(statusId: String): NetworkStatus = client.post {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/statuses/$statusId/bookmark")
        }
    }.body()

    override suspend fun unbookmarkStatus(statusId: String): NetworkStatus = client.post {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/statuses/$statusId/unbookmark")
        }
    }.body()
}