package social.firefly.feature.thread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import social.firefly.common.Resource
import social.firefly.common.tree.TreeNode
import social.firefly.common.tree.toDepthList
import social.firefly.common.tree.toTree
import social.firefly.common.utils.edit
import social.firefly.core.analytics.FeedLocation
import social.firefly.core.analytics.ThreadAnalytics
import social.firefly.core.datastore.UserPreferences
import social.firefly.core.datastore.UserPreferencesDatastore
import social.firefly.core.model.Status
import social.firefly.core.model.Thread
import social.firefly.core.ui.postcard.DepthLinesUiState
import social.firefly.core.ui.postcard.ExpandRepliesButtonUiState
import social.firefly.core.ui.postcard.PostCardDelegate
import social.firefly.core.ui.postcard.PostCardUiState
import social.firefly.core.ui.postcard.toPostCardUiState
import social.firefly.core.usecase.mastodon.account.GetLoggedInUserAccountId
import social.firefly.core.usecase.mastodon.thread.GetThread

class ThreadViewModel(
    private val analytics: ThreadAnalytics,
    private val getThread: GetThread,
    private val mainStatusId: String,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    private val userPreferences: UserPreferencesDatastore,
) : ViewModel(), ThreadInteractions {

    private val loggedInAccountId = getLoggedInUserAccountId()

    private val postIdsWithHiddenReplies = MutableStateFlow<List<String>>(emptyList())
    private val postIdsIgnoringMaxReplyCount = MutableStateFlow<List<String>>(emptyList())

    val postCardDelegate: PostCardDelegate by inject(
        PostCardDelegate::class.java,
    ) { parametersOf(
        FeedLocation.THREAD,
        { statusId: String ->
            postIdsWithHiddenReplies.update {
                postIdsWithHiddenReplies.value.toMutableList().apply {
                    if (contains(statusId)) {
                        remove(statusId)
                    } else {
                        add(statusId)
                    }
                }
            }
        }
    ) }

    private val _uiState = MutableStateFlow<Resource<ThreadPostCardCollection>>(Resource.Loading())
    val uiState = _uiState.asStateFlow()

    private var getThreadJob: Job? = null
    private var cachedData: ThreadPostCardCollection? = null

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

    init {
        loadThread()
    }

    private fun loadThread() {
        getThreadJob?.cancel()
        getThreadJob = viewModelScope.launch {
            val threadDataFlow: Flow<ThreadData> = getThread(mainStatusId).map { threadResource ->
                when (threadResource) {
                    is Resource.Loading -> ThreadData(
                        threadResource = Resource.Loading()
                    )
                    is Resource.Error -> ThreadData(
                        threadResource = threadResource
                    )
                    is Resource.Loaded -> {
                        ThreadData(
                            threadResource = threadResource,
                            repliesTree = buildList {
                                add(threadResource.data.status)
                                addAll(threadResource.data.context.descendants)
                            }.toTree(
                                identifier = { it.statusId },
                                parentIdentifier = { it.inReplyToId },
                            )
                        )
                    }
                }
            }
            combine(
                threadType,
                threadDataFlow,
                postIdsWithHiddenReplies,
                postIdsIgnoringMaxReplyCount,
            ) { threadType, threadData, postsIdsWithHiddenReplies, postIdsIgnoringMaxReplyCount ->
                when (val threadResource = threadData.threadResource) {
                    is Resource.Loading -> {
                        cachedData?.let { Resource.Loading(cachedData) } ?: Resource.Loading()
                    }
                    is Resource.Error -> {
                        Resource.Error(threadResource.exception)
                    }
                    is Resource.Loaded -> {
                        val data = generateThreadPostCardCollection(
                            threadType = threadType,
                            threadData = threadData,
                            postsIdsWithHiddenReplies = postsIdsWithHiddenReplies,
                            postIdsIgnoringMaxReplyCount = postIdsIgnoringMaxReplyCount,
                        )
                        cachedData = data
                        Resource.Loaded(data)
                    }
                }
            }.collect {
                _uiState.edit { it }
            }
        }
    }

    private fun generateThreadPostCardCollection(
        threadType: ThreadType,
        threadData: ThreadData,
        postsIdsWithHiddenReplies: List<String>,
        postIdsIgnoringMaxReplyCount: List<String>,
    ): ThreadPostCardCollection {
        val thread = threadData.threadResource.data!!
        return when (threadType) {
            ThreadType.LIST -> {
                val mapToPostCardUiState = fun Status.(): PostCardUiState = toPostCardUiState(
                    currentUserAccountId = loggedInAccountId,
                    isClickable = statusId != mainStatusId,
                )
                ThreadPostCardCollection(
                    ancestors = thread.context.ancestors.map { it.mapToPostCardUiState() },
                    mainPost = thread.status.mapToPostCardUiState(),
                    descendants = thread.context.descendants.map {
                        ThreadDescendant.PostCard(it.mapToPostCardUiState())
                    },
                )
            }

            ThreadType.DIRECT_REPLIES -> {
                val mapToPostCardUiState = fun Status.(): PostCardUiState = toPostCardUiState(
                    currentUserAccountId = loggedInAccountId,
                    showTopRowMetaData = false,
                    isClickable = statusId != mainStatusId,
                )
                ThreadPostCardCollection(
                    ancestors = thread.context.ancestors.map { it.mapToPostCardUiState() },
                    mainPost = thread.status.mapToPostCardUiState(),
                    descendants = thread.context.descendants
                        .filter { it.inReplyToId == mainStatusId }
                        .map { ThreadDescendant.PostCard(it.mapToPostCardUiState()) },
                )
            }

            ThreadType.TREE -> {
                val descendants = threadData.repliesTree?.toDepthList(
                    shouldIgnoreChildren = {
                        postsIdsWithHiddenReplies.contains(it.statusId)
                    },
                    shouldAddAllChildrenBeyondLimit = {
                        postIdsIgnoringMaxReplyCount.contains(it.statusId)
                    },
                    childLimit = MAX_REPLY_COUNT,
                )?.drop(1)?.filter {
                    it.depth <= MAX_DEPTH
                }?.map { depthItem ->
                    if (depthItem.hiddenRepliesCount == -1) {
                        val status = depthItem.value
                        val isAtMaxDepth = depthItem.depth == MAX_DEPTH
                        val hasReplies = depthItem.hasReplies
                        ThreadDescendant.PostCard(
                            status.toPostCardUiState(
                                currentUserAccountId = loggedInAccountId,
                                depthLinesUiState = DepthLinesUiState(
                                    postDepth = depthItem.depth,
                                    depthLines = depthItem.depthLines,
                                    showViewMoreRepliesText = isAtMaxDepth && hasReplies,
                                    expandRepliesButtonUiState = when {
                                        !hasReplies || isAtMaxDepth -> ExpandRepliesButtonUiState.HIDDEN
                                        postsIdsWithHiddenReplies.contains(
                                            status.statusId
                                        ) -> ExpandRepliesButtonUiState.PLUS
                                        else -> ExpandRepliesButtonUiState.MINUS
                                    },
                                ),
                                showTopRowMetaData = false,
                            )
                        )
                    } else {
                        ThreadDescendant.ViewMore(
                            depthLinesUiState = DepthLinesUiState(
                                postDepth = depthItem.depth,
                                depthLines = depthItem.depthLines,
                                showViewMoreRepliesText = false,
                                expandRepliesButtonUiState = ExpandRepliesButtonUiState.HIDDEN,
                                showViewMoreRepliesWithPlusButton = true,
                            ),
                            count = depthItem.hiddenRepliesCount,
                            statusId = depthItem.value.statusId,
                        )
                    }
                } ?: emptyList()

                val mapToPostCardUiState = fun Status.(): PostCardUiState = toPostCardUiState(
                    currentUserAccountId = loggedInAccountId,
                    showTopRowMetaData = false,
                    isClickable = statusId != mainStatusId,
                )
                ThreadPostCardCollection(
                    ancestors = thread.context.ancestors.map { it.mapToPostCardUiState() },
                    mainPost = thread.status.mapToPostCardUiState(),
                    descendants = descendants,
                )
            }
        }
    }

    override fun onRetryClicked() {
        loadThread()
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

    override fun onShowAllRepliesClicked(statusId: String) {
        postIdsIgnoringMaxReplyCount.update {
            postIdsIgnoringMaxReplyCount.value.toMutableList().apply {
                add(statusId)
            }
        }
    }

    override fun onPulledToRefresh() {
        loadThread()
    }

    override fun onsScreenViewed() {
        analytics.threadScreenViewed()
    }

    companion object {
        private const val MAX_DEPTH = 6
        private const val MAX_REPLY_COUNT = 2
    }

    private data class ThreadData(
        val threadResource: Resource<Thread>,
        val repliesTree: TreeNode<Status>? = null
    )
}
