package social.firefly.core.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import social.firefly.core.database.model.entities.hashtagCollections.TrendingHashTag
import social.firefly.core.database.model.entities.hashtagCollections.TrendingHashTagWrapper
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.HashtagRepository
import social.firefly.core.repository.mastodon.TrendingHashtagRepository
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class TrendingHashtagsRemoteMediator(
    val repository: TrendingHashtagRepository,
    val hashtagRepository: HashtagRepository,
    val databaseDelegate: DatabaseDelegate,
) : RemoteMediator<Int, TrendingHashTagWrapper>() {
    private var nextPositionIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TrendingHashTagWrapper>
    ): MediatorResult {
        return try {
            var pageSize: Int = state.config.pageSize
            val hashtags =
                when (loadType) {
                    LoadType.REFRESH -> {
                        nextPositionIndex = 0
                        pageSize = state.config.initialLoadSize
                        repository.getTrendingTags(
                            limit = pageSize,
                            offset = nextPositionIndex,
                        )
                    }

                    LoadType.PREPEND -> {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    LoadType.APPEND -> {
                        repository.getTrendingTags(
                            limit = pageSize,
                            offset = nextPositionIndex,
                        )
                    }
                }

            databaseDelegate.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    nextPositionIndex = 0
                }
                hashtagRepository.insertAll(hashtags)
                repository.insertAll(
                    hashtags.mapIndexed { index, hashTag ->
                        TrendingHashTag(
                            hashTag.name,
                            position = nextPositionIndex + index,
                        )
                    }
                )
            }

            nextPositionIndex += hashtags.size

            // There seems to be some race condition for refreshes.  Subsequent pages do
            // not get loaded because once we return a mediator result, the next append
            // and prepend happen right away.  The paging source doesn't have enough time
            // to collect the initial page from the database, so the [state] we get as
            // a parameter in this load method doesn't have any data in the pages, so
            // it's assumed we've reached the end of pagination, and nothing gets loaded
            // ever again.
            if (loadType == LoadType.REFRESH) {
                delay(REFRESH_DELAY)
            }

            MediatorResult.Success(
                endOfPaginationReached = when (loadType) {
                    LoadType.REFRESH,
                    LoadType.APPEND -> hashtags.isEmpty()

                    else -> true
                },
            )
        } catch (e: Exception) {
            Timber.e(e)
            MediatorResult.Error(e)
        }
    }

    companion object {
        private const val REFRESH_DELAY = 200L
    }
}
