package social.firefly.core.usecase.mastodon.search

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import social.firefly.common.Resource
import social.firefly.common.utils.launchSupervisor
import social.firefly.core.model.SearchResultDetailed
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.HashtagRepository
import social.firefly.core.repository.mastodon.RelationshipRepository
import social.firefly.core.repository.mastodon.SearchRepository
import social.firefly.core.repository.mastodon.model.search.toSearchedAccount
import social.firefly.core.repository.mastodon.model.search.toSearchedHashTags
import social.firefly.core.repository.mastodon.model.search.toSearchedStatus
import social.firefly.core.usecase.mastodon.status.GetInReplyToAccountNames
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase
import timber.log.Timber

class SearchAll(
    private val searchRepository: SearchRepository,
    private val accountRepository: AccountRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val hashtagRepository: HashtagRepository,
    private val databaseDelegate: DatabaseDelegate,
    private val relationshipRepository: RelationshipRepository,
    private val getInReplyToAccountNames: GetInReplyToAccountNames,
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
        coroutineScope.launchSupervisor {
            try {
                val searchResult = searchRepository.search(
                    query = query,
                    limit = limit,
                )

                val relationshipsJob = async {
                    accountRepository.getAccountRelationships(
                        searchResult.accounts.map { it.accountId }
                    )
                }

                val statuses = getInReplyToAccountNames(searchResult.statuses)
                val relationships = relationshipsJob.await()

                databaseDelegate.withTransaction {
                    accountRepository.insertAll(searchResult.accounts)
                    relationshipRepository.insertAll(relationships)
                    saveStatusToDatabase.invoke(statuses)
                    hashtagRepository.insertAll(searchResult.hashtags)

                    searchRepository.deleteSearchResults()
                    searchRepository.insertAllAccounts(searchResult.accounts.toSearchedAccount())
                    searchRepository.insertAllStatuses(statuses.toSearchedStatus())
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