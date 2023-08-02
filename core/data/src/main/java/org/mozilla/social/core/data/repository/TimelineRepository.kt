package org.mozilla.social.core.data.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.mozilla.social.core.data.repository.model.toExternalModel
import org.mozilla.social.core.network.AccountApi
import org.mozilla.social.core.network.TimelineApi
import org.mozilla.social.core.network.model.NetworkStatus
import org.mozilla.social.model.Status

class TimelineRepository internal constructor(
    private val timelineApi: TimelineApi,
    private val accountApi: AccountApi,
) {
    suspend fun retrieveHomeTimeline(): List<Status> =
        timelineApi.getHomeTimeline().getInReplyToAccountNames()

    suspend fun retrievePublicTimeline(): List<Status> =
        timelineApi.getPublicTimeline().getInReplyToAccountNames()

    private suspend fun List<NetworkStatus>.getInReplyToAccountNames(): List<Status> =
        coroutineScope {
            map { status ->
                // get in reply to account names
                async {
                    status.inReplyToAccountId?.let { accountId ->
                        status.toExternalModel(
                            inReplyToAccountName = accountApi.getAccount(accountId).displayName
                        )
                    } ?: status.toExternalModel()
                }
            }.map {
                it.await()
            }
        }
}