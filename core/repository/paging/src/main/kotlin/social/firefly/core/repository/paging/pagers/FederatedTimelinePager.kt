package social.firefly.core.repository.paging.pagers

import androidx.paging.PagingSource
import social.firefly.core.database.model.entities.statusCollections.FederatedTimelineStatusWrapper
import social.firefly.core.model.PageItem
import social.firefly.core.model.Status
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.TimelineRepository
import social.firefly.core.repository.mastodon.model.status.toExternalModel
import social.firefly.core.repository.paging.IdBasedPager
import social.firefly.core.usecase.mastodon.status.GetInReplyToAccountNames
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase

class FederatedTimelinePager(
    private val timelineRepository: TimelineRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val databaseDelegate: DatabaseDelegate,
    private val getInReplyToAccountNames: GetInReplyToAccountNames,
) : IdBasedPager<Status, FederatedTimelineStatusWrapper> {
    override fun mapDbObjectToExternalModel(dbo: FederatedTimelineStatusWrapper): Status =
        dbo.status.toExternalModel()

    override suspend fun saveLocally(items: List<PageItem<Status>>, isRefresh: Boolean) {
        databaseDelegate.withTransaction {
            if (isRefresh) timelineRepository.deleteFederatedTimeline()

            val statuses = items.map { it.item }

            saveStatusToDatabase(statuses)
            timelineRepository.insertAllIntoFederatedTimeline(statuses)
        }
    }

    override suspend fun getRemotely(limit: Int, nextKey: String?): MastodonPagedResponse<Status> {
        val response = timelineRepository.getPublicTimeline(
            federatedOnly = true,
            maxId = nextKey,
            limit = limit,
        )

        return getInReplyToAccountNames(response)
    }

    override fun pagingSource(): PagingSource<Int, FederatedTimelineStatusWrapper> =
        timelineRepository.getFederatedTimelinePagingSource()
}