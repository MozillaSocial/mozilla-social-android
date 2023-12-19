package org.mozilla.social.core.repository.mastodon

import org.mozilla.social.core.database.dao.SearchDao
import org.mozilla.social.core.database.model.entities.accountCollections.SearchedAccount
import org.mozilla.social.core.database.model.entities.hashtagCollections.SearchedHashTag
import org.mozilla.social.core.database.model.entities.statusCollections.SearchedStatus
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.model.HashTag
import org.mozilla.social.core.model.SearchResult
import org.mozilla.social.core.model.SearchType
import org.mozilla.social.core.network.mastodon.SearchApi
import org.mozilla.social.core.repository.mastodon.model.hashtag.toExternalModel
import org.mozilla.social.core.repository.mastodon.model.search.toExternal
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel

class SearchRepository internal constructor(
    private val searchApi: SearchApi,
    private val searchDao: SearchDao,
) {
    suspend fun searchForAccounts(query: String): List<Account> {
        return searchApi.search(
            query,
            SearchType.Accounts.value,
        ).accounts.map { it.toExternalModel() }
    }

    suspend fun searchForHashtags(query: String): List<HashTag> {
        return searchApi.search(
            query,
            SearchType.Hashtags.value,
        ).hashtags.map { it.toExternalModel() }
    }

    suspend fun search(
        query: String,
        type: SearchType? = null,
        resolve: Boolean = false,
        accountId: String? = null,
        excludeUnreviewed: Boolean = false,
        limit: Int? = null,
        offset: Int? = null,
    ) : SearchResult = searchApi.search(
        query = query,
        type = type?.value,
        resolve = resolve,
        accountId = accountId,
        excludeUnreviewed = excludeUnreviewed,
        limit = limit,
        offset = offset,
    ).toExternal()

    fun insertAllAccounts(
        searchedAccounts: List<SearchedAccount>
    ) = searchDao.upsertAccounts(searchedAccounts)

    fun insertAllStatuses(
        searchedStatuses: List<SearchedStatus>
    ) = searchDao.upsertStatuses(searchedStatuses)

    fun insertAllHashTags(
        searchedHashTags: List<SearchedHashTag>
    ) = searchDao.upsertHashTags(searchedHashTags)
}
