package social.firefly.core.network.mastodon.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.network.mastodon.DomainBlocksApi
import social.firefly.core.network.mastodon.utils.toMastodonPagedResponse

class DomainBlocksApiImpl(
    private val client: HttpClient,
) : DomainBlocksApi {

    override suspend fun getBlockedDomains(
        maxId: String?,
        sinceId: String?,
        minId: String?,
        limit: Int?,
    ): MastodonPagedResponse<String> = client.get {
        url {
            protocol = URLProtocol.HTTPS
            path("api/v1/domain_blocks")
        }
    }.toMastodonPagedResponse<String, String> {
        it
    }
}