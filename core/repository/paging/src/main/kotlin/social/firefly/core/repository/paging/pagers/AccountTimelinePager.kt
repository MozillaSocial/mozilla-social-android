package social.firefly.core.repository.paging.pagers

import androidx.paging.PagingSource
import social.firefly.core.database.model.entities.statusCollections.AccountTimelineStatusWrapper
import social.firefly.core.model.AccountTimelineType
import social.firefly.core.model.PageItem
import social.firefly.core.model.Status
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.TimelineRepository
import social.firefly.core.repository.mastodon.model.status.toExternalModel
import social.firefly.core.repository.paging.IdBasedPager
import social.firefly.core.usecase.mastodon.status.GetInReplyToAccountNames
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase

class AccountTimelinePager(
    private val accountId: String,
    private val timelineType: AccountTimelineType,
    private val accountRepository: AccountRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val timelineRepository: TimelineRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val getInReplyToAccountNames: GetInReplyToAccountNames,
) : IdBasedPager<Status, AccountTimelineStatusWrapper> {
    override fun mapDbObjectToExternalModel(dbo: AccountTimelineStatusWrapper): Status =
        dbo.status.toExternalModel()

    override suspend fun saveLocally(items: List<PageItem<Status>>, isRefresh: Boolean) {
        databaseDelegate.withTransaction {
            if (isRefresh) {
                timelineRepository.deleteAccountTimeline(accountId, timelineType)
            }

            val statuses = items.map { it.item }

            saveStatusToDatabase(statuses)
            timelineRepository.insertAllIntoAccountTimeline(statuses, timelineType)
        }
    }

    override suspend fun getRemotely(limit: Int, nextKey: String?): MastodonPagedResponse<Status> {
        val response = accountRepository.getAccountStatuses(
            accountId = accountId,
            maxId = nextKey,
            minId = null,
            loadSize = limit,
            onlyMedia = timelineType == AccountTimelineType.MEDIA,
            excludeReplies = timelineType == AccountTimelineType.POSTS,
        )

        return getInReplyToAccountNames(response)
    }

    override fun pagingSource(): PagingSource<Int, AccountTimelineStatusWrapper> =
        timelineRepository.accountTimelinePagingSource(
            accountId = accountId,
            timelineType = timelineType,
        )
}