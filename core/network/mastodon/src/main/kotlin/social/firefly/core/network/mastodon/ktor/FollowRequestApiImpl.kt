package social.firefly.core.network.mastodon.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.http.URLProtocol
import io.ktor.http.path
import social.firefly.core.network.mastodon.FollowRequestApi
import social.firefly.core.network.mastodon.model.responseBody.NetworkRelationship

class FollowRequestApiImpl(
    private val client: HttpClient,
) : FollowRequestApi {

    override suspend fun acceptFollowRequest(
        accountId: String,
    ): NetworkRelationship = client.post {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/follow_requests/$accountId/authorize")
        }
    }.body()

    override suspend fun rejectFollowRequest(
        accountId: String,
    ): NetworkRelationship = client.post {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/follow_requests/$accountId/reject")
        }
    }.body()
}