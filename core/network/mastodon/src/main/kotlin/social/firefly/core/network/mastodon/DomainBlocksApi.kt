package social.firefly.core.network.mastodon

interface DomainBlocksApi {

    suspend fun getBlockedDomains(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
    ): List<String>
}