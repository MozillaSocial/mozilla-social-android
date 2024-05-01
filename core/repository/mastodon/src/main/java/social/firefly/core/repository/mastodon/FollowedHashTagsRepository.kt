package social.firefly.core.repository.mastodon

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import social.firefly.core.database.dao.FollowedHashTagsDao
import social.firefly.core.database.model.entities.hashtagCollections.FollowedHashTagWrapper
import social.firefly.core.model.HashTag
import social.firefly.core.network.mastodon.FollowedTagsApi
import social.firefly.core.repository.mastodon.model.hashtag.toExternalModel

class FollowedHashTagsRepository(
    private val api: FollowedTagsApi,
    private val dao: FollowedHashTagsDao,
) {

    suspend fun getFollowedHashTags(
        olderThanId: String? = null,
        newerThanId: String? = null,
        immediatelyNewerThanId: String? = null,
        limit: Int? = null,
    ): List<HashTag> = api.getFollowedHashTags(
        olderThanId = olderThanId,
        newerThanId = newerThanId,
        immediatelyNewerThanId = immediatelyNewerThanId,
        limit = limit,
    ).map { it.toExternalModel() }

    @ExperimentalPagingApi
    fun getFollowedHashTagPager(
        remoteMediator: RemoteMediator<Int, FollowedHashTagWrapper>,
        pageSize: Int = 40,
        initialLoadSize: Int = 40,
    ): Flow<PagingData<HashTag>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            initialLoadSize = initialLoadSize,
        ),
        remoteMediator = remoteMediator,
    ) {
        dao.pagingSource()
    }.flow.map { pagingData ->
        pagingData.map {
            it.hashTag.toExternalModel()
        }
    }
}