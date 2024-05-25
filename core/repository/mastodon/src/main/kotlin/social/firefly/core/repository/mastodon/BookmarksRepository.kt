package social.firefly.core.repository.mastodon

import androidx.paging.PagingSource
import social.firefly.core.database.dao.BookmarksDao
import social.firefly.core.database.model.entities.statusCollections.BookmarksTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.BookmarksTimelineStatusWrapper
import social.firefly.core.model.Status
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.network.mastodon.BookmarksApi
import social.firefly.core.network.mastodon.utils.toMastodonPagedResponse
import social.firefly.core.repository.mastodon.model.status.toExternalModel

class BookmarksRepository(
    private val api: BookmarksApi,
    private val dao: BookmarksDao,
) {
    fun pagingSource(): PagingSource<Int, BookmarksTimelineStatusWrapper> =
        dao.bookmarksTimelinePagingSource()

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