package social.firefly.core.usecase.mastodon.status

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import social.firefly.common.annotations.PreferUseCase
import social.firefly.common.utils.StringFactory
import social.firefly.core.database.model.entities.statusCollections.BookmarksTimelineStatus
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.BookmarksRepository
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.StatusRepository
import social.firefly.core.usecase.mastodon.R

class UndoBookmarkStatus internal constructor(
    private val externalScope: CoroutineScope,
    private val databaseDelegate: DatabaseDelegate,
    private val statusRepository: StatusRepository,
    private val bookmarksRepository: BookmarksRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val showSnackbar: ShowSnackbar,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(statusId: String) =
        externalScope.async(dispatcherIo) {
            var bookmarkedStatus: BookmarksTimelineStatus? = null
            try {
                bookmarkedStatus = bookmarksRepository.getStatusFromTimeline(statusId)
                databaseDelegate.withTransaction {
                    statusRepository.updateBookmarked(statusId, false)
                    bookmarksRepository.deleteStatusFromTimeline(statusId)
                }
                val status = statusRepository.unbookmarkStatus(statusId)
                saveStatusToDatabase(status)
            } catch (e: Exception) {
                databaseDelegate.withTransaction {
                    statusRepository.updateBookmarked(statusId, true)
                    bookmarkedStatus?.let { bookmarksRepository.insert(it) }
                }
                showSnackbar(
                    text = StringFactory.resource(R.string.error_removing_bookmark),
                    isError = true,
                )
                throw UndoBookmarkStatusFailedException(e)
            }
        }.await()

    class UndoBookmarkStatusFailedException(e: Exception) : Exception(e)
}