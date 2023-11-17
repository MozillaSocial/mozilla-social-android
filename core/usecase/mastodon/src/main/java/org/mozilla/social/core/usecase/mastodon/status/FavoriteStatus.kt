package org.mozilla.social.core.usecase.mastodon.status

import androidx.room.withTransaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.repository.mastodon.StatusRepository
import org.mozilla.social.core.usecase.mastodon.R

class FavoriteStatus internal constructor(
    private val externalScope: CoroutineScope,
    private val socialDatabase: SocialDatabase,
    private val statusRepository: StatusRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val showSnackbar: ShowSnackbar,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(statusId: String) =
        externalScope.async(dispatcherIo) {
            try {
                socialDatabase.withTransaction {
                    statusRepository.updateFavoriteCount(statusId, 1)
                    statusRepository.updateFavorited(statusId, true)
                }
                val status = statusRepository.favoriteStatus(statusId)
                saveStatusToDatabase(status)
            } catch (e: Exception) {
                socialDatabase.withTransaction {
                    statusRepository.updateFavoriteCount(statusId, -1)
                    statusRepository.updateFavorited(statusId, false)
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
