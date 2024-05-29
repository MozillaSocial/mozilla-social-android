package social.firefly.core.repository.paging.pagers.status

import androidx.paging.PagingSource
import social.firefly.core.database.model.entities.statusCollections.HashTagTimelineStatusWrapper
import social.firefly.core.model.PageItem
import social.firefly.core.model.Status
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.TimelineRepository
import social.firefly.core.repository.mastodon.model.status.toExternalModel
import social.firefly.core.repository.paging.IdBasedPager
import social.firefly.core.usecase.mastodon.status.GetInReplyToAccountNames
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase

class HashTagTimelinePager(
    private val timelineRepository: TimelineRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val databaseDelegate: DatabaseDelegate,
    private val getInReplyToAccountNames: GetInReplyToAccountNames,
    private val hashTag: String,
) : IdBasedPager<Status, HashTagTimelineStatusWrapper> {
    override fun mapDbObjectToExternalModel(dbo: HashTagTimelineStatusWrapper): Status =
        dbo.status.toExternalModel()

    override suspend fun saveLocally(items: List<PageItem<Status>>, isRefresh: Boolean) {
        databaseDelegate.withTransaction {
            if (isRefresh) timelineRepository.deleteHashTagTimeline(hashTag)

            val statuses = items.map { it.item }

            saveStatusToDatabase(statuses)
            timelineRepository.insertAllIntoHashTagTimeline(hashTag, statuses)
        }
    }

    override suspend fun getRemotely(limit: Int, nextKey: String?): MastodonPagedResponse<Status> {
        val response = timelineRepository.getHashtagTimeline(
            hashTag = hashTag,
            maxId = nextKey,
            limit = limit,
        )

        return getInReplyToAccountNames(response)
    }

    override fun pagingSource(): PagingSource<Int, HashTagTimelineStatusWrapper> =
        timelineRepository.getHashTagPagingSource(hashTag)
}