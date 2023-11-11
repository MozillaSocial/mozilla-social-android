package org.mozilla.social.core.usecase.mastodon.status

import androidx.room.withTransaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.usecase.mastodon.R

class DeleteStatus(
    private val externalScope: CoroutineScope,
    private val statusApi: org.mozilla.social.core.network.mastodon.StatusApi,
    private val socialDatabase: SocialDatabase,
    private val showSnackbar: ShowSnackbar,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {

    suspend operator fun invoke(
        statusId: String,
    ) = externalScope.async(dispatcherIo) {
        try {
            socialDatabase.statusDao().updateIsBeingDeleted(statusId, true)
            statusApi.deleteStatus(statusId)
            socialDatabase.withTransaction {
                socialDatabase.homeTimelineDao().deletePost(statusId)
                socialDatabase.localTimelineDao().deletePost(statusId)
                socialDatabase.federatedTimelineDao().deletePost(statusId)
                socialDatabase.hashTagTimelineDao().deletePost(statusId)
                socialDatabase.accountTimelineDao().deletePost(statusId)
                socialDatabase.statusDao().deleteStatus(statusId)
            }
        } catch (e: Exception) {
            socialDatabase.statusDao().updateIsBeingDeleted(statusId, false)
            showSnackbar(
                text = StringFactory.resource(R.string.error_deleting_post),
                isError = true,
            )
            throw DeleteStatusFailedException(e)
        }
    }.await()

    class DeleteStatusFailedException(e: Exception) : Exception(e)
}