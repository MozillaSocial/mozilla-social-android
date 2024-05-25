package social.firefly.core.network.mastodon.ktor

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import social.firefly.core.network.mastodon.BlocksApi
import social.firefly.core.network.mastodon.model.Response
import social.firefly.core.network.mastodon.model.responseBody.NetworkAccount
import social.firefly.core.network.mastodon.utils.toResponse

class BlocksApiImpl(
    private val client: HttpClient
) : BlocksApi {

    override suspend fun getBlocks(
        maxId: String?,
        sinceId: String?,
        minId: String?,
        limit: Int?
    ): Response<List<NetworkAccount>> = client.get {
        url { _ ->
            protocol = URLProtocol.HTTPS
            path("api/v1/blocks")
            parameters.apply {
                maxId?.let { append("max_id", it) }
                sinceId?.let { append("since_id", it) }
                minId?.let { append("min_id", it) }
                limit?.let { append("limit", it.toString()) }
            }
        }
    }.toResponse()
}