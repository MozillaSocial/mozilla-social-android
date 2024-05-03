package social.firefly.core.usecase.mastodon.status

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import social.firefly.common.annotations.PreferUseCase
import social.firefly.common.utils.StringFactory
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.StatusRepository
import social.firefly.core.usecase.mastodon.R

class BookmarkStatus internal constructor(
    private val externalScope: CoroutineScope,
    private val databaseDelegate: DatabaseDelegate,
    private val statusRepository: StatusRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val showSnackbar: ShowSnackbar,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(statusId: String) =
        externalScope.async(dispatcherIo) {
            try {
                databaseDelegate.withTransaction {
                    statusRepository.updateBookmarked(statusId, true)
                }
                val status = statusRepository.bookmarkStatus(statusId)
                saveStatusToDatabase(status)
            } catch (e: Exception) {
                databaseDelegate.withTransaction {
                    statusRepository.updateBookmarked(statusId, false)
                }
                showSnackbar(
                    text = StringFactory.resource(R.string.error_adding_bookmark),
                    isError = true,
                )
                throw BookmarkStatusFailedException(e)
            }
        }.await()

    class BookmarkStatusFailedException(e: Exception) : Exception(e)
}
