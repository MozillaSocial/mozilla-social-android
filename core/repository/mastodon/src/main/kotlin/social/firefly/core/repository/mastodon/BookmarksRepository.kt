package social.firefly.core.repository.mastodon

import social.firefly.core.database.dao.BookmarksDao
import social.firefly.core.database.model.entities.statusCollections.BookmarksTimelineStatus
import social.firefly.core.model.Status
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.network.mastodon.BookmarksApi
import social.firefly.core.repository.mastodon.model.status.toExternalModel
import social.firefly.core.repository.mastodon.model.toMastodonPagedResponse

class BookmarksRepository(
    private val api: BookmarksApi,
    private val dao: BookmarksDao,
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

    suspend fun deleteBookmarksTimeline() {
        dao.deleteBookmarksTimelines()
    }

    suspend fun insertAll(
        statuses: List<BookmarksTimelineStatus>
    ) = dao.upsertAll(statuses)

    suspend fun insert(
        status: BookmarksTimelineStatus
    ) = dao.upsert(status)

    suspend fun deleteStatusFromTimeline(
        statusId: String,
    ) = dao.deletePost(statusId)

    suspend fun getStatusFromTimeline(
        statusId: String,
    ): BookmarksTimelineStatus = dao.getStatus(statusId)
}