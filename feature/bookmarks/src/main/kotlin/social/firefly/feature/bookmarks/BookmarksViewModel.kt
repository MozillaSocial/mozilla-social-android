package social.firefly.feature.bookmarks

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.map
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import social.firefly.core.analytics.FeedLocation
import social.firefly.core.repository.paging.pagers.BookmarksPager
import social.firefly.core.ui.postcard.PostCardDelegate
import social.firefly.core.ui.postcard.toPostCardUiState
import social.firefly.core.usecase.mastodon.account.GetLoggedInUserAccountId

class BookmarksViewModel(
    bookmarksPager: BookmarksPager,
    userAccountId: GetLoggedInUserAccountId,
) : ViewModel(), KoinComponent {

    private val loggedInUserId = userAccountId()

    val postCardDelegate by inject<PostCardDelegate> {
        parametersOf(FeedLocation.BOOKMARKS)
    }

    @OptIn(ExperimentalPagingApi::class)
    val feed = bookmarksPager.build()
        .map { pagingData ->
            pagingData.map { status ->
                status.toPostCardUiState(
                    currentUserAccountId = loggedInUserId,
                    shouldShowUnbookmarkConfirmation = true,
                )
            }
        }
}