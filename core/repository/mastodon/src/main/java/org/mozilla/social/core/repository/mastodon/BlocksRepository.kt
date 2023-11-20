package org.mozilla.social.core.repository.mastodon

import org.mozilla.social.common.parseMastodonLinkHeader
import org.mozilla.social.core.model.paging.AccountPagingWrapper
import org.mozilla.social.core.network.mastodon.BlocksApi
import org.mozilla.social.core.network.mastodon.model.NetworkAccount
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel
import retrofit2.HttpException
import retrofit2.Response

class BlocksRepository(private val api: BlocksApi) {
    suspend fun getBlocks(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: String? = null,
    ): AccountPagingWrapper {
        val response = api.getBlocks(
            maxId = maxId,
            sinceId = sinceId,
            minId = minId,
            limit = limit
        )

        if (!response.isSuccessful) {
            throw HttpException(response)
        }

        return AccountPagingWrapper(
            accounts = response.toAccountsList(),
            pagingLinks = response.toPagingLinks(),
        )
    }
}

private fun Response<List<NetworkAccount>>.toAccountsList() =
    body()?.map { it.toExternalModel() } ?: emptyList()

private fun Response<List<NetworkAccount>>.toPagingLinks() =
    headers().get("link")?.parseMastodonLinkHeader()