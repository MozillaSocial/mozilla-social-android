package org.mozilla.social.core.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import org.mozilla.social.core.database.model.entities.hashtagCollections.SearchedHashTagWrapper
import org.mozilla.social.core.model.SearchType
import org.mozilla.social.core.repository.mastodon.DatabaseDelegate
import org.mozilla.social.core.repository.mastodon.HashtagRepository
import org.mozilla.social.core.repository.mastodon.SearchRepository
import org.mozilla.social.core.repository.mastodon.model.search.toSearchedAccount
import org.mozilla.social.core.repository.mastodon.model.search.toSearchedHashTags
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class SearchedHashTagsRemoteMediator(
    private val searchRepository: SearchRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val hashtagRepository: HashtagRepository,
    private val query: String,
) : RemoteMediator<Int, SearchedHashTagWrapper>() {
    private var nextPositionIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SearchedHashTagWrapper>
    ): MediatorResult {
        return try {
            var pageSize: Int = state.config.pageSize
            val response =
                when (loadType) {
                    LoadType.REFRESH -> {
                        nextPositionIndex = 0
                        pageSize = state.config.initialLoadSize
                        searchRepository.search(
                            query = query,
                            type = SearchType.Hashtags,
                            limit = pageSize,
                            offset = nextPositionIndex,
                        )
                    }

                    LoadType.PREPEND -> {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    LoadType.APPEND -> {
                        searchRepository.search(
                            query = query,
                            type = SearchType.Hashtags,
                            limit = pageSize,
                            offset = nextPositionIndex,
                        )
                    }
                }

            databaseDelegate.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    nextPositionIndex = 0
                }

                hashtagRepository.insertAll(response.hashtags)
                searchRepository.insertAllHashTags(
                    response.hashtags.toSearchedHashTags(startIndex = nextPositionIndex)
                )
            }

            nextPositionIndex += response.hashtags.size

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
                    LoadType.APPEND -> response.hashtags.isEmpty()
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