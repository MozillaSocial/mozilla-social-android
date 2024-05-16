package social.firefly.core.repository.mastodon

import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.network.mastodon.DomainBlocksApi

class DomainBlocksRepository(
    private val api: DomainBlocksApi,
) {

    suspend fun getDomainBlocks(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
    ): MastodonPagedResponse<String> = api.getBlockedDomains(
        maxId = maxId,
        sinceId = sinceId,
        minId = minId,
        limit = limit,
    )
}