package social.firefly.core.network.mastodon.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.URLProtocol
import io.ktor.http.path
import social.firefly.core.network.mastodon.TagsApi
import social.firefly.core.network.mastodon.model.responseBody.NetworkHashTag

class TagsApiImpl(
    private val client: HttpClient,
) : TagsApi {
    override suspend fun followHashTag(
        hashTag: String,
    ): NetworkHashTag = client.post {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/tags/$hashTag/follow")
        }
    }.body()

    override suspend fun unfollowHashTag(hashTag: String): NetworkHashTag = client.post {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/tags/$hashTag/unfollow")
        }
    }.body()

    override suspend fun getHashTag(hashTag: String): NetworkHashTag = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/tags/$hashTag")
        }
    }.body()

}