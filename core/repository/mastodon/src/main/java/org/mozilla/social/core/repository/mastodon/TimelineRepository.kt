package org.mozilla.social.core.repository.mastodon

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mozilla.social.common.parseMastodonLinkHeader
import org.mozilla.social.core.database.dao.AccountTimelineStatusDao
import org.mozilla.social.core.database.dao.FederatedTimelineStatusDao
import org.mozilla.social.core.database.dao.HomeTimelineStatusDao
import org.mozilla.social.core.database.dao.LocalTimelineStatusDao
import org.mozilla.social.core.database.model.accountCollections.FolloweeWrapper
import org.mozilla.social.core.database.model.statusCollections.AccountTimelineStatusWrapper
import org.mozilla.social.core.database.model.statusCollections.toStatusWrapper
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.model.StatusVisibility
import org.mozilla.social.core.model.paging.StatusPagingWrapper
import org.mozilla.social.core.model.wrappers.DetailedAccountWrapper
import org.mozilla.social.core.network.mastodon.TimelineApi
import org.mozilla.social.core.repository.mastodon.model.account.toDetailedAccount
import org.mozilla.social.core.repository.mastodon.model.status.toAccountTimelineStatus
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel
import org.mozilla.social.core.repository.mastodon.model.status.toFederatedTimelineStatus
import org.mozilla.social.core.repository.mastodon.model.status.toHomeTimelineStatus
import org.mozilla.social.core.repository.mastodon.model.status.toLocalTimelineStatus
import retrofit2.HttpException

class TimelineRepository internal constructor(
    private val timelineApi: TimelineApi,
    private val homeTimelineStatusDao: HomeTimelineStatusDao,
    private val federatedTimelineStatusDao: FederatedTimelineStatusDao,
    private val localTimelineStatusDao: LocalTimelineStatusDao,
    private val accountTimelineStatusDao: AccountTimelineStatusDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    suspend fun getHomeTimeline(
        olderThanId: String? = null,
        immediatelyNewerThanId: String? = null,
        loadSize: Int? = null,
    ): StatusPagingWrapper {
        val response =
            timelineApi.getHomeTimeline(
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
        val response =
            timelineApi.getPublicTimeline(
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
        val response =
            timelineApi.getHashTagTimeline(
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

    fun insertAllIntoHomeTimeline(statuses: List<Status>) {
        homeTimelineStatusDao.insertAll(statuses.map { it.toHomeTimelineStatus() })
    }

    suspend fun getPostsFromHomeTimelineForAccount(accountId: String): List<Status> =
        homeTimelineStatusDao.getPostsFromAccount(accountId).map { it.toStatusWrapper().toExternalModel() }

    fun insertAllIntoLocalTimeline(statuses: List<Status>) =
        localTimelineStatusDao.insertAll(statuses.map { it.toLocalTimelineStatus() })

    fun insertAllIntoFederatedTimeline(statuses: List<Status>) =
        federatedTimelineStatusDao.insertAll(statuses.map { it.toFederatedTimelineStatus() })

    suspend fun insertStatusIntoTimelines(status: Status) =
        withContext(ioDispatcher) {
            homeTimelineStatusDao.insert(status.toHomeTimelineStatus())
            if (status.visibility == StatusVisibility.Public) {
                localTimelineStatusDao.insert(status.toLocalTimelineStatus())
                federatedTimelineStatusDao.insert(status.toFederatedTimelineStatus())
            }
        }

    fun insertAllIntoAccountTimeline(statuses: List<Status>) =
        accountTimelineStatusDao.insertAll(statuses.map { it.toAccountTimelineStatus() })

    @ExperimentalPagingApi
    fun getAccountTimelinePager(
        accountId: String,
        remoteMediator: RemoteMediator<Int, AccountTimelineStatusWrapper>,
        pageSize: Int = 40,
        initialLoadSize: Int = 40,
    ): Flow<PagingData<Status>> =
        Pager(
            config =
            PagingConfig(
                pageSize = pageSize,
                initialLoadSize = initialLoadSize,
            ),
            remoteMediator = remoteMediator,
        ) {
            accountTimelineStatusDao.accountTimelinePagingSource(accountId)
        }.flow.map { pagingData ->
            pagingData.map {
                it.toStatusWrapper().toExternalModel()
            }
        }

    suspend fun deleteAccountTimeline(accountId: String) =
        accountTimelineStatusDao.deleteAccountTimeline(accountId)

    suspend fun deleteStatusFromAccountTimeline(statusId: String) =
        accountTimelineStatusDao.deletePost(statusId)
}
