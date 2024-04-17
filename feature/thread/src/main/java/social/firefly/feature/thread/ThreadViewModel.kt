package social.firefly.feature.thread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import social.firefly.core.analytics.FeedLocation
import social.firefly.core.analytics.ThreadAnalytics
import social.firefly.core.ui.postcard.DepthLinesUiState
import social.firefly.core.ui.postcard.PostCardDelegate
import social.firefly.core.ui.postcard.PostCardUiState
import social.firefly.core.ui.postcard.toPostCardUiState
import social.firefly.core.usecase.mastodon.account.GetLoggedInUserAccountId
import social.firefly.core.usecase.mastodon.thread.GetThread
import timber.log.Timber

class ThreadViewModel(
    private val analytics: ThreadAnalytics,
    getThread: GetThread,
    mainStatusId: String,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
) : ViewModel(), ThreadInteractions {
    var statuses: Flow<List<PostCardUiState>> =
        getThread.invoke(mainStatusId).map { statuses ->
            statuses.mapIndexed { index, statusWithDepth ->
                statusWithDepth.status.toPostCardUiState(
                    currentUserAccountId = getLoggedInUserAccountId(),
                    postCardInteractions = postCardDelegate,
                    depthLinesUiState = DepthLinesUiState(
                        postDepth = statusWithDepth.depth,
                        depthLines = buildList {
                            if (statusWithDepth.depth == -1) {
                                return@buildList
                            }
                            for (i in 0..<statusWithDepth.depth) {
                                add(i)
                            }
                            if (statusWithDepth.status.repliesCount > 0) {
                                add(statusWithDepth.depth)
                            }
                        }
                    )
                )
            }
        }.catch {
            Timber.e(it)
        }

    override fun onsScreenViewed() {
        analytics.threadScreenViewed()
    }

    val postCardDelegate: PostCardDelegate by KoinJavaComponent.inject(
        PostCardDelegate::class.java,
    ) { parametersOf(viewModelScope, FeedLocation.THREAD) }
}
