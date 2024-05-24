package social.firefly.core.repository.mastodon.model.search

import social.firefly.core.model.SearchResult
import social.firefly.core.network.mastodon.model.responseBody.NetworkSearchResult
import social.firefly.core.repository.mastodon.model.hashtag.toExternalModel
import social.firefly.core.repository.mastodon.model.status.toExternalModel

fun social.firefly.core.network.mastodon.model.responseBody.NetworkSearchResult.toExternal() = SearchResult(
    accounts = accounts.map { it.toExternalModel() },
    statuses = statuses.map { it.toExternalModel() },
    hashtags = hashtags.map { it.toExternalModel() }
)