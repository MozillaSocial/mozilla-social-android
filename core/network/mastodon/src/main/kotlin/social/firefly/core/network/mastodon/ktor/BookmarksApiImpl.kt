package social.firefly.core.network.mastodon.ktor

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import social.firefly.core.network.mastodon.BookmarksApi
import social.firefly.core.network.mastodon.model.Response
import social.firefly.core.network.mastodon.model.responseBody.NetworkStatus
import social.firefly.core.network.mastodon.utils.toResponse

class BookmarksApiImpl(
    private val client: HttpClient,
) : BookmarksApi {

    override suspend fun getBookmarks(
        maxId: String?,
        sinceId: String?,
        minId: String?,
        limit: Int?
    ): Response<List<NetworkStatus>> = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/bookmarks")
        }
    }.toResponse()
}