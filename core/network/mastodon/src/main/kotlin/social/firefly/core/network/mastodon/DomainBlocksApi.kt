package social.firefly.core.network.mastodon

import social.firefly.core.model.paging.MastodonPagedResponse

interface DomainBlocksApi {

    suspend fun getBlockedDomains(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
    ): MastodonPagedResponse<String>

    suspend fun blockDomain(
        domain: String,
    )

    suspend fun unblockDomain(
        domain: String,
    )
}