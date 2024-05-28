package social.firefly.core.repository.paging.pagers.status

import androidx.paging.PagingSource
import social.firefly.core.database.model.entities.statusCollections.LocalTimelineStatusWrapper
import social.firefly.core.model.PageItem
import social.firefly.core.model.Status
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.TimelineRepository
import social.firefly.core.repository.mastodon.model.status.toExternalModel
import social.firefly.core.repository.paging.IdBasedPager
import social.firefly.core.usecase.mastodon.status.GetInReplyToAccountNames
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase

class LocalTimelinePager(
    private val timelineRepository: TimelineRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val databaseDelegate: DatabaseDelegate,
    private val getInReplyToAccountNames: GetInReplyToAccountNames,
) : IdBasedPager<Status, LocalTimelineStatusWrapper> {
    override fun mapDbObjectToExternalModel(dbo: LocalTimelineStatusWrapper): Status =
        dbo.status.toExternalModel()

    override suspend fun saveLocally(items: List<PageItem<Status>>, isRefresh: Boolean) {
        databaseDelegate.withTransaction {
            if (isRefresh) timelineRepository.deleteLocalTimeline()

            val statuses = items.map { it.item }

            saveStatusToDatabase(statuses)
            timelineRepository.insertAllIntoLocalTimeline(statuses)
        }
    }

    override suspend fun getRemotely(limit: Int, nextKey: String?): MastodonPagedResponse<Status> {
        val response = timelineRepository.getPublicTimeline(
            localOnly = true,
            maxId = nextKey,
            limit = limit,
        )

        return getInReplyToAccountNames(response)
    }

    override fun pagingSource(): PagingSource<Int, LocalTimelineStatusWrapper> =
        timelineRepository.getLocalTimelinePagingSource()
}