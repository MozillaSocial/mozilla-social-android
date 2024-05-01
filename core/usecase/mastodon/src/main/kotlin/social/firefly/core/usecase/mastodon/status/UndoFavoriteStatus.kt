package social.firefly.core.usecase.mastodon.status

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import social.firefly.common.annotations.PreferUseCase
import social.firefly.common.utils.StringFactory
import social.firefly.core.database.model.entities.statusCollections.FavoritesTimelineStatus
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.FavoritesRepository
import social.firefly.core.repository.mastodon.StatusRepository
import social.firefly.core.usecase.mastodon.R

class UndoFavoriteStatus internal constructor(
    private val externalScope: CoroutineScope,
    private val databaseDelegate: DatabaseDelegate,
    private val statusRepository: StatusRepository,
    private val favoritesRepository: FavoritesRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val showSnackbar: ShowSnackbar,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(statusId: String) =
        externalScope.async(dispatcherIo) {
            var favoriteStatus: FavoritesTimelineStatus? = null
            try {
                favoriteStatus = favoritesRepository.getStatusFromTimeline(statusId)
                databaseDelegate.withTransaction {
                    statusRepository.updateFavoriteCount(statusId, -1)
                    statusRepository.updateFavorited(statusId, false)
                    favoritesRepository.deleteStatusFromTimeline(statusId)
                }
                val status = statusRepository.unFavoriteStatus(statusId)
                saveStatusToDatabase(status)
            } catch (e: Exception) {
                databaseDelegate.withTransaction {
                    statusRepository.updateFavoriteCount(statusId, 1)
                    statusRepository.updateFavorited(statusId, true)
                    favoriteStatus?.let { favoritesRepository.insert(it) }
                }
                showSnackbar(
                    text = StringFactory.resource(R.string.error_removing_favorite),
                    isError = true,
                )
                throw UndoFavoriteStatusFailedException(e)
            }
        }.await()

    class UndoFavoriteStatusFailedException(e: Exception) : Exception(e)
}
