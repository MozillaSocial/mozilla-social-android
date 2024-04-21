package social.firefly.feature.thread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import social.firefly.common.Resource
import social.firefly.common.tree.toDepthList
import social.firefly.common.tree.toTree
import social.firefly.common.utils.edit
import social.firefly.core.analytics.FeedLocation
import social.firefly.core.analytics.ThreadAnalytics
import social.firefly.core.datastore.UserPreferences
import social.firefly.core.datastore.UserPreferencesDatastore
import social.firefly.core.model.Status
import social.firefly.core.ui.postcard.DepthLinesUiState
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

    private val postsIdsWithHiddenReplies = MutableStateFlow<List<String>>(emptyList())

    val postCardDelegate: PostCardDelegate by KoinJavaComponent.inject(
        PostCardDelegate::class.java,
    ) { parametersOf(
        FeedLocation.THREAD,
        { statusId: String ->
            postsIdsWithHiddenReplies.update {
                postsIdsWithHiddenReplies.value.toMutableList().apply {
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
            combine(
                threadType,
                getThread(mainStatusId),
                postsIdsWithHiddenReplies
            ) { threadType, threadResource, postsIdsWithHiddenReplies ->
                val thread = threadResource.data ?: return@combine when (threadResource) {
                    is Resource.Loading -> Resource.Loading()
                    is Resource.Error -> Resource.Error(threadResource.exception)
                    is Resource.Loaded -> Resource.Loaded(ThreadPostCardCollection())
                }
                when (threadType) {
                    ThreadType.LIST -> {
                        val mapToPostCardUiState =
                            fun Status.(): PostCardUiState = toPostCardUiState(
                                currentUserAccountId = loggedInAccountId,
                                postCardInteractions = postCardDelegate,
                                isClickable = statusId != mainStatusId,
                            )
                        Resource.Loaded(
                            ThreadPostCardCollection(
                                ancestors = thread.context.ancestors.map { it.mapToPostCardUiState() },
                                mainPost = thread.status.mapToPostCardUiState(),
                                descendants = thread.context.descendants.map { it.mapToPostCardUiState() },
                            )
                        )
                    }

                    ThreadType.DIRECT_REPLIES -> {
                        val mapToPostCardUiState =
                            fun Status.(): PostCardUiState = toPostCardUiState(
                                currentUserAccountId = loggedInAccountId,
                                postCardInteractions = postCardDelegate,
                                showTopRowMetaData = false,
                                isClickable = statusId != mainStatusId,
                            )
                        Resource.Loaded(
                            ThreadPostCardCollection(
                                ancestors = thread.context.ancestors.map { it.mapToPostCardUiState() },
                                mainPost = thread.status.mapToPostCardUiState(),
                                descendants = thread.context.descendants
                                    .filter { it.inReplyToId == mainStatusId }
                                    .map { it.mapToPostCardUiState() },
                            )
                        )
                    }

                    ThreadType.TREE -> {
                        val descendants = buildList {
                            add(thread.status)
                            addAll(thread.context.descendants)
                        }.toTree(
                            identifier = { it.statusId },
                            parentIdentifier = { it.inReplyToId },
                            shouldIgnore = { postsIdsWithHiddenReplies.contains(it.statusId) }
                        )?.toDepthList()?.drop(1)?.filter {
                            it.depth <= MAX_DEPTH
                        }?.map { statusWithDepth ->
                            val isAtMaxDepth = statusWithDepth.depth == MAX_DEPTH
                            val hasReplies = statusWithDepth.value.repliesCount > 0
                            statusWithDepth.value.toPostCardUiState(
                                currentUserAccountId = loggedInAccountId,
                                postCardInteractions = postCardDelegate,
                                depthLinesUiState = DepthLinesUiState(
                                    postDepth = statusWithDepth.depth,
                                    depthLines = statusWithDepth.depthLines,
                                    startingDepth = 1,
                                    showViewMoreRepliesText = isAtMaxDepth && hasReplies,
                                    isHidingReplies = postsIdsWithHiddenReplies.contains(statusWithDepth.value.statusId),
                                ),
                                showTopRowMetaData = false,
                            )
                        } ?: emptyList()

                        val mapToPostCardUiState =
                            fun Status.(): PostCardUiState = toPostCardUiState(
                                currentUserAccountId = loggedInAccountId,
                                postCardInteractions = postCardDelegate,
                                showTopRowMetaData = false,
                                isClickable = statusId != mainStatusId,
                            )
                        Resource.Loaded(
                            ThreadPostCardCollection(
                                ancestors = thread.context.ancestors.map { it.mapToPostCardUiState() },
                                mainPost = thread.status.mapToPostCardUiState(),
                                descendants = descendants,
                            )
                        )
                    }
                }
            }.collect {
                _uiState.edit { it }
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

    override fun onsScreenViewed() {
        analytics.threadScreenViewed()
    }

    companion object {
        private const val MAX_DEPTH = 10
    }
}
