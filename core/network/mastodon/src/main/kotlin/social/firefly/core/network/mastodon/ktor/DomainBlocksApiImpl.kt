package social.firefly.core.network.mastodon.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import social.firefly.core.network.mastodon.DomainBlocksApi

class DomainBlocksApiImpl(
    private val client: HttpClient,
) : DomainBlocksApi {

    override suspend fun getBlockedDomains(
        maxId: String?,
        sinceId: String?,
        minId: String?,
        limit: Int?,
    ): List<String> = client.get {
        url {
            path("api/v1/domain_blocks")
        }
    }.body()
}