package social.firefly.core.repository.paging.pagers

import androidx.paging.PagingSource
import social.firefly.core.database.model.entities.statusCollections.BookmarksTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.BookmarksTimelineStatusWrapper
import social.firefly.core.model.PageItem
import social.firefly.core.model.Status
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.repository.mastodon.BookmarksRepository
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.model.status.toExternalModel
import social.firefly.core.repository.paging.IdBasedPager
import social.firefly.core.usecase.mastodon.status.GetInReplyToAccountNames
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase

class BookmarksPager(
    private val bookmarksRepository: BookmarksRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val databaseDelegate: DatabaseDelegate,
    private val getInReplyToAccountNames: GetInReplyToAccountNames,
) : IdBasedPager<Status, BookmarksTimelineStatusWrapper> {
    override fun mapDbObjectToExternalModel(dbo: BookmarksTimelineStatusWrapper): Status =
        dbo.status.toExternalModel()

    override suspend fun saveLocally(items: List<PageItem<Status>>, isRefresh: Boolean) {
        databaseDelegate.withTransaction {
            if (isRefresh) bookmarksRepository.deleteBookmarksTimeline()
            saveStatusToDatabase(items.map { it.item })
            bookmarksRepository.insertAll(
                items.map { item ->
                    BookmarksTimelineStatus(
                        statusId = item.item.statusId,
                        position = item.position,
                    )
                }
            )
        }
    }

    override suspend fun getRemotely(limit: Int, nextKey: String?): MastodonPagedResponse<Status> {
        val response = bookmarksRepository.getBookmarksPage(
            maxId = nextKey,
            limit = limit,
        )

        return getInReplyToAccountNames(response)
    }

    override fun pagingSource(): PagingSource<Int, BookmarksTimelineStatusWrapper> =
        bookmarksRepository.pagingSource()
}