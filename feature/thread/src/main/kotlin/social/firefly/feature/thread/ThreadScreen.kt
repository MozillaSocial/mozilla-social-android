package social.firefly.feature.thread

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import social.firefly.common.Resource
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.UiConstants
import social.firefly.core.ui.common.appbar.FfCloseableTopAppBar
import social.firefly.core.ui.common.dropdown.FfDropDownItem
import social.firefly.core.ui.common.dropdown.FfIconButtonDropDownMenu
import social.firefly.core.ui.common.error.GenericError
import social.firefly.core.ui.common.loading.MaxSizeLoading
import social.firefly.core.ui.common.pullrefresh.PullRefreshIndicator
import social.firefly.core.ui.common.pullrefresh.pullRefresh
import social.firefly.core.ui.common.pullrefresh.rememberPullRefreshState
import social.firefly.core.ui.common.text.MediumTextLabel
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.PostCardListItem
import social.firefly.core.ui.postcard.components.DepthLines

@Composable
internal fun ThreadScreen(
    threadStatusId: String,
    viewModel: ThreadViewModel = koinViewModel(parameters = { parametersOf(threadStatusId) }),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val threadType by viewModel.threadType.collectAsStateWithLifecycle(
        initialValue = ThreadType.TREE
    )

    ThreadScreen(
        threadType = threadType,
        uiState = uiState,
        postCardDelegate = viewModel.postCardDelegate,
        threadInteractions = viewModel,
    )

    LaunchedEffect(Unit) {
        viewModel.onsScreenViewed()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThreadScreen(
    threadType: ThreadType,
    uiState: Resource<ThreadPostCardCollection>,
    postCardDelegate: PostCardInteractions,
    threadInteractions: ThreadInteractions,
) {
    FfSurface {
        Column(Modifier.systemBarsPadding()) {
            FfCloseableTopAppBar(
                title = stringResource(id = R.string.thread_screen_title),
                actions = {
                    ThreadTypeButton(
                        threadType = threadType,
                        threadInteractions = threadInteractions,
                    )
                }
            )

            val refreshState = rememberPullRefreshState(
                refreshing = uiState is Resource.Loading,
                onRefresh = { threadInteractions.onPulledToRefresh() },
            )

            Box(
                modifier = Modifier
                    .pullRefresh(refreshState)
                    .fillMaxSize(),
            ) {
                if (uiState.data == null && uiState is Resource.Error) {
                    GenericError(
                        onRetryClicked = { threadInteractions.onRetryClicked() }
                    )
                }

                uiState.data?.let {
                    ThreadList(
                        modifier = Modifier
                            .widthIn(max = UiConstants.MAX_WIDTH)
                            .align(Alignment.TopCenter),
                        threadType = threadType,
                        statuses = it,
                        postCardDelegate = postCardDelegate,
                        threadInteractions = threadInteractions,
                    )
                }

                PullRefreshIndicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    refreshing = uiState is Resource.Loading,
                    state = refreshState
                )
            }
        }
    }
}

@Composable
private fun ThreadList(
    threadType: ThreadType,
    statuses: ThreadPostCardCollection,
    postCardDelegate: PostCardInteractions,
    threadInteractions: ThreadInteractions,
    modifier: Modifier = Modifier,
) {
    if (statuses.mainPost == null) return

    val listState = rememberSaveable(
        saver = LazyListState.Saver,
        key = statuses.mainPost.statusId
    ) {
        LazyListState(
            firstVisibleItemIndex = statuses.ancestors.size,
            firstVisibleItemScrollOffset = 0,
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        state = listState,
    ) {
        items(
            count = statuses.ancestors.count(),
            key = { statuses.ancestors[it].statusId },
        ) { index ->
            val item = statuses.ancestors[index]
            PostCardListItem(
                uiState = item,
                postCardInteractions = postCardDelegate,
                showDivider = true,
            )
        }

        item {
            PostCardListItem(
                uiState = statuses.mainPost,
                postCardInteractions = postCardDelegate,
                showDivider = false,
            )
        }

        item {
            if (statuses.descendants.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(FfTheme.colors.layer2),
                ) {
                    MediumTextLabel(
                        modifier = Modifier
                            .padding(start = 8.dp, top = 16.dp, bottom = 16.dp)
                            .align(Alignment.CenterVertically),
                        text = stringResource(id = R.string.replies),
                    )
                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        painter = FfIcons.arrowDropDown(),
                        contentDescription = null,
                    )
                }
            }
        }

        descendants(
            threadType = threadType,
            statuses = statuses,
            postCardDelegate = postCardDelegate,
            threadInteractions = threadInteractions
        )
    }
}

private fun LazyListScope.descendants(
    threadType: ThreadType,
    statuses: ThreadPostCardCollection,
    postCardDelegate: PostCardInteractions,
    threadInteractions: ThreadInteractions,
) {
    items(
        count = statuses.descendants.count(),
        key = { statuses.descendants[it].id },
    ) { index ->
        when (val item = statuses.descendants[index]) {
            is ThreadDescendant.PostCard -> {
                PostCardListItem(
                    uiState = item.uiState,
                    postCardInteractions = postCardDelegate,
                    showDivider = threadType != ThreadType.TREE,
                )
            }
            is ThreadDescendant.ViewMore -> {
                Row(
                    modifier = Modifier
                        .height(36.dp)
                        .fillMaxWidth()
                ) {
                    DepthLines(depthLinesUiState = item.depthLinesUiState)
                    Row(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .align(Alignment.CenterVertically)
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { threadInteractions.onShowAllRepliesClicked(item.statusId) }
                            .padding(2.dp),
                    ) {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .background(FfTheme.colors.layer1)
                                .size(20.dp),
                            painter = FfIcons.plusCircle(),
                            contentDescription = null
                        )
                        MediumTextLabel(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .align(Alignment.CenterVertically),
                            text = pluralStringResource(
                                id = R.plurals.show_more_replies,
                                count = item.count,
                                item.count,
                            )
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun ThreadTypeButton(
    threadType: ThreadType,
    threadInteractions: ThreadInteractions,
) {
    val overflowMenuExpanded = remember { mutableStateOf(false) }

    FfIconButtonDropDownMenu(
        expanded = overflowMenuExpanded,
        dropDownMenuContent = {
            for (dropDownOption in ThreadType.entries) {
                FfDropDownItem(
                    text = when (dropDownOption) {
                        ThreadType.LIST -> {
                            stringResource(id = R.string.list_view)
                        }
                        ThreadType.DIRECT_REPLIES -> {
                            stringResource(id = R.string.direct_replies_list_view)
                        }
                        ThreadType.TREE -> {
                            stringResource(id = R.string.tree_view)
                        }
                    },
                    icon = {
                        Icon(
                            modifier = Modifier
                                .size(FfIcons.Sizes.small),
                            painter = when (dropDownOption) {
                                ThreadType.LIST -> {
                                    FfIcons.listPlus()
                                }
                                ThreadType.DIRECT_REPLIES -> {
                                    FfIcons.list()
                                }
                                ThreadType.TREE -> {
                                    FfIcons.treeView()
                                }
                            },
                            contentDescription = null
                        )
                    },
                    expanded = overflowMenuExpanded,
                    onClick = { threadInteractions.onThreadTypeSelected(dropDownOption) },
                )
            }
        }
    ) {
        Icon(
            modifier = Modifier
                .size(FfIcons.Sizes.normal),
            painter = when (threadType) {
                ThreadType.LIST -> {
                    FfIcons.listPlus()
                }
                ThreadType.DIRECT_REPLIES -> {
                    FfIcons.list()
                }
                ThreadType.TREE -> {
                    FfIcons.treeView()
                }
            },
            contentDescription = stringResource(id = R.string.view_type)
        )
    }
}
