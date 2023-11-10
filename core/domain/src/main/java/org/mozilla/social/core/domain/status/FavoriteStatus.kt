package org.mozilla.social.core.domain.status

import androidx.room.withTransaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.domain.R
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.network.mastodon.StatusApi

class FavoriteStatus(
    private val externalScope: CoroutineScope,
    private val statusApi: org.mozilla.social.core.network.mastodon.StatusApi,
    private val socialDatabase: SocialDatabase,
    private val statusRepository: StatusRepository,
    private val showSnackbar: ShowSnackbar,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {

    suspend operator fun invoke(
        statusId: String,
    ) = externalScope.async(dispatcherIo) {
        try {
            socialDatabase.withTransaction {
                socialDatabase.statusDao().updateFavoriteCount(statusId, 1)
                socialDatabase.statusDao().updateFavorited(statusId, true)
            }
            val status = statusApi.favoriteStatus(statusId).toExternalModel()
            statusRepository.saveStatusToDatabase(status)
        } catch (e: Exception) {
            socialDatabase.withTransaction {
                socialDatabase.statusDao().updateFavoriteCount(statusId, -1)
                socialDatabase.statusDao().updateFavorited(statusId, false)
            }
            showSnackbar(
                text = StringFactory.resource(R.string.error_adding_favorite),
                isError = true,
            )
            throw FavoriteStatusFailedException(e)
        }
    }.await()

    class FavoriteStatusFailedException(e: Exception) : Exception(e)
}