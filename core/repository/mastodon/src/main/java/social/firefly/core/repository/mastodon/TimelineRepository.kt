package social.firefly.core.repository.mastodon

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
import retrofit2.HttpException
import social.firefly.common.parseMastodonLinkHeader
import social.firefly.core.database.dao.AccountTimelineStatusDao
import social.firefly.core.database.dao.FederatedTimelineStatusDao
import social.firefly.core.database.dao.HashTagTimelineStatusDao
import social.firefly.core.database.dao.HomeTimelineStatusDao
import social.firefly.core.database.dao.LocalTimelineStatusDao
import social.firefly.core.database.model.entities.statusCollections.AccountTimelineStatusWrapper
import social.firefly.core.database.model.entities.statusCollections.FederatedTimelineStatusWrapper
import social.firefly.core.database.model.entities.statusCollections.HashTagTimelineStatusWrapper
import social.firefly.core.database.model.entities.statusCollections.HomeTimelineStatusWrapper
import social.firefly.core.database.model.entities.statusCollections.LocalTimelineStatusWrapper
import social.firefly.core.model.AccountTimelineType
import social.firefly.core.model.Status
import social.firefly.core.model.StatusVisibility
import social.firefly.core.model.paging.StatusPagingWrapper
import social.firefly.core.network.mastodon.TimelineApi
import social.firefly.core.repository.mastodon.model.status.toAccountTimelineStatus
import social.firefly.core.repository.mastodon.model.status.toExternalModel
import social.firefly.core.repository.mastodon.model.status.toFederatedTimelineStatus
import social.firefly.core.repository.mastodon.model.status.toHashTagTimelineStatus
import social.firefly.core.repository.mastodon.model.status.toHomeTimelineStatus
import social.firefly.core.repository.mastodon.model.status.toLocalTimelineStatus

class TimelineRepository internal constructor(
    private val timelineApi: TimelineApi,
    private val homeTimelineStatusDao: HomeTimelineStatusDao,
    private val federatedTimelineStatusDao: FederatedTimelineStatusDao,
    private val localTimelineStatusDao: LocalTimelineStatusDao,
    private val accountTimelineStatusDao: AccountTimelineStatusDao,
    private val hashTagTimelineStatusDao: HashTagTimelineStatusDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

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

    suspend fun insertStatusIntoTimelines(status: Status) =
        withContext(ioDispatcher) {
            homeTimelineStatusDao.upsert(status.toHomeTimelineStatus())
            if (status.visibility == StatusVisibility.Public) {
                localTimelineStatusDao.upsert(status.toLocalTimelineStatus())
                federatedTimelineStatusDao.upsert(status.toFederatedTimelineStatus())
            }
        }

    //region Local timeline
    @ExperimentalPagingApi
    fun getLocalTimelinePager(
        remoteMediator: RemoteMediator<Int, LocalTimelineStatusWrapper>,
        pageSize: Int = 20,
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
            localTimelineStatusDao.localTimelinePagingSource()
        }.flow.map { pagingData ->
            pagingData.map {
                it.status.toExternalModel()
            }
        }

    suspend fun insertAllIntoLocalTimeline(statuses: List<Status>) =
        localTimelineStatusDao.upsertAll(statuses.map { it.toLocalTimelineStatus() })

    suspend fun deleteLocalTimeline() = localTimelineStatusDao.deleteLocalTimeline()

    suspend fun removePostInLocalTimelineForAccount(accountId: String) =
        localTimelineStatusDao.removePostsFromAccount(accountId)
    //endregion

    //region Federated timeline
    @ExperimentalPagingApi
    fun getFederatedTimelinePager(
        remoteMediator: RemoteMediator<Int, FederatedTimelineStatusWrapper>,
        pageSize: Int = 20,
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
            federatedTimelineStatusDao.federatedTimelinePagingSource()
        }.flow.map { pagingData ->
            pagingData.map {
                it.status.toExternalModel()
            }
        }

    suspend fun insertAllIntoFederatedTimeline(statuses: List<Status>) =
        federatedTimelineStatusDao.upsertAll(statuses.map { it.toFederatedTimelineStatus() })

    suspend fun deleteFederatedTimeline() = federatedTimelineStatusDao.deleteFederatedTimeline()

    suspend fun removePostsFromFederatedTimelineForAccount(accountId: String) =
        federatedTimelineStatusDao.removePostsFromAccount(accountId)
    //endregion

    //region Home timeline
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

    @ExperimentalPagingApi
    fun getHomeTimelinePager(
        remoteMediator: RemoteMediator<Int, HomeTimelineStatusWrapper>,
        pageSize: Int = 20,
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
            homeTimelineStatusDao.homeTimelinePagingSource()
        }.flow.map { pagingData ->
            pagingData.map {
                it.status.toExternalModel()
            }
        }

    suspend fun insertAllIntoHomeTimeline(statuses: List<Status>) =
        homeTimelineStatusDao.upsertAll(statuses.map { it.toHomeTimelineStatus() })

    suspend fun getPostsFromHomeTimelineForAccount(accountId: String): List<Status> =
        homeTimelineStatusDao.getPostsFromAccount(accountId).map { it.status.toExternalModel() }

    suspend fun deleteHomeTimeline() = homeTimelineStatusDao.deleteHomeTimeline()

    suspend fun deleteHomeStatusesBeforeId(statusId: String) =
        homeTimelineStatusDao.deleteStatusesBeforeId(statusId)

    suspend fun removePostInHomeTimelineForAccount(accountId: String) =
        homeTimelineStatusDao.removePostsFromAccount(accountId)
    //endregion

    //region Hashtag timeline
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

    @ExperimentalPagingApi
    fun getHashtagTimelinePager(
        hashTag: String,
        remoteMediator: RemoteMediator<Int, HashTagTimelineStatusWrapper>,
        pageSize: Int = 20,
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
            hashTagTimelineStatusDao.hashTagTimelinePagingSource(hashTag)
        }.flow.map { pagingData ->
            pagingData.map {
                it.status.toExternalModel()
            }
        }

    suspend fun deleteHashTagTimeline(hashTag: String) =
        hashTagTimelineStatusDao.deleteHashTagTimeline(hashTag)

    suspend fun deleteAllHashTagTimelines() = hashTagTimelineStatusDao.deleteAllHashTagTimelines()

    suspend fun insertAllIntoHashTagTimeline(
        hashTag: String,
        statuses: List<Status>
    ) = hashTagTimelineStatusDao.upsertAll(statuses.map { it.toHashTagTimelineStatus(hashTag) })
    //endregion

    //region Account timeline
    suspend fun insertAllIntoAccountTimeline(
        statuses: List<Status>,
        timelineType: AccountTimelineType,
    ) =
        accountTimelineStatusDao.upsertAll(statuses.map { it.toAccountTimelineStatus(timelineType) })

    @ExperimentalPagingApi
    fun getAccountTimelinePager(
        accountId: String,
        timelineType: AccountTimelineType,
        remoteMediator: RemoteMediator<Int, AccountTimelineStatusWrapper>,
        pageSize: Int = 20,
        initialLoadSize: Int = 40,
    ): Flow<PagingData<Status>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            initialLoadSize = initialLoadSize,
        ),
        remoteMediator = remoteMediator,
    ) {
        accountTimelineStatusDao.accountTimelinePagingSource(accountId, timelineType)
    }.flow.map { pagingData ->
        pagingData.map {
            it.status.toExternalModel()
        }
    }

    suspend fun deleteAccountTimeline(
        accountId: String,
        timelineType: AccountTimelineType,
    ) = accountTimelineStatusDao.deleteAccountTimeline(accountId, timelineType)

    suspend fun deleteAllAccountTimelines(
        accountsToKeep: List<String> = emptyList()
    ) = accountTimelineStatusDao.deleteAllAccountTimelines(
        accountsToKeep
    )
    //endregion
}
