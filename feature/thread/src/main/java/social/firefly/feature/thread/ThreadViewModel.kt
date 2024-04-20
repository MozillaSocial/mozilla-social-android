package social.firefly.feature.thread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import social.firefly.common.tree.toDepthList
import social.firefly.common.tree.toTree
import social.firefly.core.analytics.FeedLocation
import social.firefly.core.analytics.ThreadAnalytics
import social.firefly.core.datastore.UserPreferences
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

    private val loggedInAccountId = getLoggedInUserAccountId()

    @OptIn(ExperimentalCoroutinesApi::class)
    val threadType = userPreferences.threadType.mapLatest {
        when (it) {
            social.firefly.core.datastore.UserPreferences.ThreadType.LIST -> {
                ThreadType.LIST
            }
            social.firefly.core.datastore.UserPreferences.ThreadType.DIRECT_REPLIES_LIST -> {
                ThreadType.DIRECT_REPLIES
            }
            else -> {
                ThreadType.TREE
            }
        }
    }

    private val innerStatuses = getThread.invoke(mainStatusId)
        .catch {
            Timber.e(it)
        }

    var statuses: Flow<List<PostCardUiState>> = threadType.combine(innerStatuses) { thread, statuses ->
        when (thread) {
            ThreadType.LIST -> {
                statuses.map {
                    it.toPostCardUiState(
                        currentUserAccountId = loggedInAccountId,
                        postCardInteractions = postCardDelegate,
                    )
                }
            }
            ThreadType.DIRECT_REPLIES -> {
                val mainStatusIndex = statuses.indexOf(statuses.find { it.statusId == mainStatusId })
                val ancestors = statuses.subList(0, mainStatusIndex + 1)
                val directReplies = statuses.filter { it.inReplyToId == mainStatusId }
                buildList {
                    addAll(ancestors)
                    addAll(directReplies)
                }.map {
                    it.toPostCardUiState(
                        currentUserAccountId = loggedInAccountId,
                        postCardInteractions = postCardDelegate,
                        showTopRowMetaData = false,
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
                        currentUserAccountId = loggedInAccountId,
                        postCardInteractions = postCardDelegate,
                        depthLinesUiState = DepthLinesUiState(
                            postDepth = statusWithDepth.depth,
                            depthLines = statusWithDepth.depthLines,
                            startingDepth = mainStatusDepth + 1,
                        ),
                        showTopRowMetaData = false,
                    )
                }
            }
        }
    }

    override fun onThreadTypeSelected(threadType: ThreadType) {
        viewModelScope.launch {
            userPreferences.saveThreadType(
                when (threadType) {
                    ThreadType.LIST -> UserPreferences.ThreadType.LIST
                    ThreadType.DIRECT_REPLIES -> UserPreferences.ThreadType.DIRECT_REPLIES_LIST
                    ThreadType.TREE -> UserPreferences.ThreadType.TREE
                }
            )
        }
    }

    override fun onsScreenViewed() {
        analytics.threadScreenViewed()
    }

    val postCardDelegate: PostCardDelegate by KoinJavaComponent.inject(
        PostCardDelegate::class.java,
    ) { parametersOf(viewModelScope, FeedLocation.THREAD) }
}
