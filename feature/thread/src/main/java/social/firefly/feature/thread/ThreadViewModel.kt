package social.firefly.feature.thread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import social.firefly.common.tree.toDepthList
import social.firefly.common.tree.toTree
import social.firefly.core.analytics.FeedLocation
import social.firefly.core.analytics.ThreadAnalytics
import social.firefly.core.datastore.UserPreferences.ThreadType
import social.firefly.core.datastore.UserPreferencesDatastore
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
    private val userPreferences: UserPreferencesDatastore,
) : ViewModel(), ThreadInteractions {

    val threadType = userPreferences.threadType

    private val innerStatuses = getThread.invoke(mainStatusId)
        .catch {
            Timber.e(it)
        }

    var statuses: Flow<List<PostCardUiState>> = threadType.combine(innerStatuses) { thread, statuses ->
        when (thread) {
            ThreadType.LIST -> {
                statuses.map {
                    it.toPostCardUiState(
                        currentUserAccountId = getLoggedInUserAccountId(),
                        postCardInteractions = postCardDelegate,
                    )
                }
            }
            ThreadType.DIRECT_REPLIES_LIST -> {
                statuses.map {
                    it.toPostCardUiState(
                        currentUserAccountId = getLoggedInUserAccountId(),
                        postCardInteractions = postCardDelegate,
                    )
                }
            }
            ThreadType.TREE -> {
                val rootNode = statuses.toTree(
                    identifier = { it.statusId },
                    parentIdentifier = { it.inReplyToId },
                )
                val depthList = rootNode?.toDepthList() ?: emptyList()
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
            }
            else -> {
                // shouldn't happen
                emptyList()
            }
        }
    }

//    var statuses: Flow<List<PostCardUiState>> =
//        getThread.invoke(mainStatusId).map { statuses ->
//            val rootNode = statuses.toTree(
//                identifier = { it.statusId },
//                parentIdentifier = { it.inReplyToId },
//            )
//            val depthList = rootNode?.toDepthList() ?: emptyList()
//            val mainStatusDepth = depthList.find { it.value.statusId == mainStatusId }?.depth ?: 0
//            depthList.map { statusWithDepth ->
//                statusWithDepth.value.toPostCardUiState(
//                    currentUserAccountId = getLoggedInUserAccountId(),
//                    postCardInteractions = postCardDelegate,
//                    depthLinesUiState = DepthLinesUiState(
//                        postDepth = statusWithDepth.depth,
//                        depthLines = statusWithDepth.depthLines,
//                        startingDepth = mainStatusDepth,
//                    )
//                )
//            }
//        }.catch {
//            Timber.e(it)
//        }

    override fun onsScreenViewed() {
        analytics.threadScreenViewed()
    }

    val postCardDelegate: PostCardDelegate by KoinJavaComponent.inject(
        PostCardDelegate::class.java,
    ) { parametersOf(viewModelScope, FeedLocation.THREAD) }
}
