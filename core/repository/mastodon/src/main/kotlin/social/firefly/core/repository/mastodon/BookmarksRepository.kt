package social.firefly.core.repository.mastodon

import social.firefly.core.model.Status
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.network.mastodon.BookmarksApi
import social.firefly.core.repository.mastodon.model.status.toExternalModel
import social.firefly.core.repository.mastodon.model.toMastodonPagedResponse

class BookmarksRepository(
    private val api: BookmarksApi,
) {
    suspend fun getBookmarksPage(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
    ): MastodonPagedResponse<Status> = api.getBookmarks(
        maxId = maxId,
        sinceId = sinceId,
        minId = minId,
        limit = limit,
    ).toMastodonPagedResponse { it.toExternalModel() }
}