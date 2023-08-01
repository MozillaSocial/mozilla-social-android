package org.mozilla.social.core.data.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.mozilla.social.core.data.repository.model.toExternalModel
import org.mozilla.social.core.network.MastodonApi
import org.mozilla.social.core.network.model.NetworkStatus
import org.mozilla.social.model.Status

class FeedRepository internal constructor(
    private val mastodonApi: MastodonApi
) {
    suspend fun retrieveHomeTimeline(): List<Status> =
        mastodonApi.getHomeTimeline().getInReplyToAccountNames()

    suspend fun retrievePublicTimeline(): List<Status> =
        mastodonApi.getPublicTimeline().getInReplyToAccountNames()

    private suspend fun List<NetworkStatus>.getInReplyToAccountNames(): List<Status> =
        coroutineScope {
            map { status ->
                // get in reply to account names
                async {
                    status.inReplyToAccountId?.let { accountId ->
                        status.toExternalModel(
                            inReplyToAccountName = mastodonApi.getAccount(accountId).displayName
                        )
                    } ?: status.toExternalModel()
                }
            }.map {
                it.await()
            }
        }
}