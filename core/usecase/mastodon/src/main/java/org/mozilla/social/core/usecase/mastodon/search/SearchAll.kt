package org.mozilla.social.core.usecase.mastodon.search

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.mozilla.social.common.Resource
import org.mozilla.social.core.model.SearchResult
import org.mozilla.social.core.model.SearchResultDetailed
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.DatabaseDelegate
import org.mozilla.social.core.repository.mastodon.HashtagRepository
import org.mozilla.social.core.repository.mastodon.RelationshipRepository
import org.mozilla.social.core.repository.mastodon.SearchRepository
import org.mozilla.social.core.repository.mastodon.model.search.toSearchedAccount
import org.mozilla.social.core.repository.mastodon.model.search.toSearchedHashTags
import org.mozilla.social.core.repository.mastodon.model.search.toSearchedStatus
import org.mozilla.social.core.usecase.mastodon.status.SaveStatusToDatabase
import timber.log.Timber

class SearchAll(
    private val searchRepository: SearchRepository,
    private val accountRepository: AccountRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val hashtagRepository: HashtagRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val relationshipRepository: RelationshipRepository,
) {

    /**
     * @param transform used to transform the search result into a UI state model of some kind
     */
    operator fun <T> invoke(
        query: String,
        coroutineScope: CoroutineScope,
        limit: Int = 8,
        transform: (searchResult: SearchResultDetailed) -> T,
    ): Flow<Resource<T>> = flow {
        emit(Resource.Loading())
        val deferred = CompletableDeferred<Resource<Unit>>()
        coroutineScope.launch {
            try {
                val searchResult = searchRepository.search(
                    query = query,
                    limit = limit,
                )

                val relationships = accountRepository.getAccountRelationships(
                    searchResult.accounts.map { it.accountId }
                )

                databaseDelegate.withTransaction {
                    accountRepository.insertAll(searchResult.accounts)
                    relationshipRepository.insertAll(relationships)
                    saveStatusToDatabase.invoke(searchResult.statuses)
                    hashtagRepository.insertAll(searchResult.hashtags)

                    searchRepository.deleteSearchResults()
                    searchRepository.insertAllAccounts(searchResult.accounts.toSearchedAccount())
                    searchRepository.insertAllStatuses(searchResult.statuses.toSearchedStatus())
                    searchRepository.insertAllHashTags(searchResult.hashtags.toSearchedHashTags())
                }

                deferred.complete(
                    Resource.Loaded(
                        Unit
                    )
                )
            } catch (e: Exception) {
                Timber.e(e)
                deferred.complete(Resource.Error(e))
            }
        }

        when (val deferredResult = deferred.await()) {
            is Resource.Error -> emit(Resource.Error(deferredResult.exception))
            else -> {
                try {
                    emitAll(
                        searchRepository.getTopSearchResultsFlow(limit).map {
                            Resource.Loaded(transform(it))
                        }
                    )
                } catch (e: Exception) {
                    Timber.e(e)
                    emit(Resource.Error(e))
                }
            }
        }
    }
}