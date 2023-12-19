package org.mozilla.social.core.repository.mastodon.model.search

import org.mozilla.social.core.model.SearchResult
import org.mozilla.social.core.network.mastodon.model.NetworkSearchResult
import org.mozilla.social.core.repository.mastodon.model.hashtag.toExternalModel
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel

fun NetworkSearchResult.toExternal() = SearchResult(
    accounts = accounts.map { it.toExternalModel() },
    statuses = statuses.map { it.toExternalModel() },
    hashtags = hashtags.map { it.toExternalModel() }
)