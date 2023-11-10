package org.mozilla.social.core.data.repository

import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.model.Account
import org.mozilla.social.model.HashTag
import org.mozilla.social.core.network.mastodon.SearchApi

class SearchRepository internal constructor(
    private val searchApi: SearchApi,
) {

    suspend fun searchForAccounts(
        query: String,
    ): List<Account> {
        return searchApi.search(
            query,
            ACCOUNTS
        ).accounts.map { it.toExternalModel() }
    }

    suspend fun searchForHashtags(
        query: String,
    ): List<HashTag> {
        return searchApi.search(
            query,
            HASHTAGS,
        ).hashtags.map { it.toExternalModel() }
    }

    companion object {
        const val ACCOUNTS = "accounts"
        const val HASHTAGS = "hashtags"
        const val STATUSES = "statuses"
    }
}