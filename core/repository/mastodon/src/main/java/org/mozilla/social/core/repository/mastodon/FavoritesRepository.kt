package org.mozilla.social.core.repository.mastodon

import org.mozilla.social.common.parseMastodonLinkHeader
import org.mozilla.social.core.model.paging.StatusPagingWrapper
import org.mozilla.social.core.network.mastodon.FavoritesApi
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel
import retrofit2.HttpException

class FavoritesRepository(
    private val api: FavoritesApi,
) {
    suspend fun getFavorites(
        olderThanId: String? = null,
        immediatelyNewerThanId: String? = null,
        loadSize: Int? = null,
    ): StatusPagingWrapper {
        val response =
            api.getFavorites(
                olderThanId = olderThanId,
                newerThanId = immediatelyNewerThanId,
                limit = loadSize,
            )

        if (!response.isSuccessful) {
            throw HttpException(response)
        }

        return StatusPagingWrapper(
            statuses = response.body()?.map { it.toExternalModel() } ?: emptyList(),
            pagingLinks = response.headers().get("link")?.parseMastodonLinkHeader(),
        )
    }
}