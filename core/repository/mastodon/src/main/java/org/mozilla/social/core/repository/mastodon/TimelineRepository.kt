package org.mozilla.social.core.repository.mastodon

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mozilla.social.common.parseMastodonLinkHeader
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.model.statusCollections.FederatedTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.HomeTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.LocalTimelineStatus
import org.mozilla.social.core.network.mastodon.TimelineApi
import org.mozilla.social.core.model.paging.StatusPagingWrapper
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.model.StatusVisibility
import retrofit2.HttpException

class TimelineRepository internal constructor(
    private val timelineApi: TimelineApi,
    private val socialDatabase: SocialDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    suspend fun getHomeTimeline(
        olderThanId: String? = null,
        immediatelyNewerThanId: String? = null,
        loadSize: Int? = null,
    ): StatusPagingWrapper {
        val response = timelineApi.getHomeTimeline(
            olderThanId = olderThanId,
            immediatelyNewerThanId = immediatelyNewerThanId,
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

    suspend fun getPublicTimeline(
        localOnly: Boolean? = null,
        federatedOnly: Boolean? = null,
        mediaOnly: Boolean? = null,
        olderThanId: String? = null,
        immediatelyNewerThanId: String? = null,
        loadSize: Int? = null,
    ): StatusPagingWrapper {
        val response = timelineApi.getPublicTimeline(
            localOnly = localOnly,
            federatedOnly = federatedOnly,
            mediaOnly = mediaOnly,
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


    suspend fun getHashtagTimeline(
        hashTag: String,
        olderThanId: String? = null,
        immediatelyNewerThanId: String? = null,
        loadSize: Int? = null,
    ): StatusPagingWrapper {
        val response = timelineApi.getHashTagTimeline(
            hashTag = hashTag,
            olderThanId = olderThanId,
            immediatelyNewerThanId = immediatelyNewerThanId,
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


    suspend fun insertStatusIntoTimelines(status: Status) = withContext(ioDispatcher) {
        socialDatabase.homeTimelineDao().insert(
            HomeTimelineStatus(
                statusId = status.statusId,
                accountId = status.account.accountId,
                pollId = status.poll?.pollId,
                boostedStatusId = status.boostedStatus?.statusId,
                boostedPollId = status.boostedStatus?.poll?.pollId,
                boostedStatusAccountId = status.boostedStatus?.account?.accountId,
            )
        )

        if (status.visibility == StatusVisibility.Public) {
            socialDatabase.localTimelineDao().insert(
                LocalTimelineStatus(
                    statusId = status.statusId,
                    accountId = status.account.accountId,
                    pollId = status.poll?.pollId,
                    boostedStatusId = status.boostedStatus?.statusId,
                    boostedPollId = status.boostedStatus?.poll?.pollId,
                    boostedStatusAccountId = status.boostedStatus?.account?.accountId,
                )
            )
            socialDatabase.federatedTimelineDao().insert(
                FederatedTimelineStatus(
                    statusId = status.statusId,
                    accountId = status.account.accountId,
                    pollId = status.poll?.pollId,
                    boostedStatusId = status.boostedStatus?.statusId,
                    boostedPollId = status.boostedStatus?.poll?.pollId,
                    boostedStatusAccountId = status.boostedStatus?.account?.accountId,
                )
            )
        }
    }
}
