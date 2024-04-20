package social.firefly.feature.thread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import social.firefly.common.tree.toDepthList
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
        getThread.invoke(mainStatusId).map { rootNode ->
            val depthList = rootNode.toDepthList()
            val mainStatusDepth = depthList.find { it.value.statusId == mainStatusId }?.depth ?: 0
            depthList.map { statusWithDepth ->
                statusWithDepth.value.toPostCardUiState(
                    currentUserAccountId = getLoggedInUserAccountId(),
                    postCardInteractions = postCardDelegate,
                    depthLinesUiState = DepthLinesUiState(
                        postDepth = statusWithDepth.depth,
                        depthLines = statusWithDepth.depthLines,
                        startingDepth = mainStatusDepth,
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
