package social.firefly.feature.thread

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import social.firefly.common.Resource
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.appbar.FfCloseableTopAppBar
import social.firefly.core.ui.common.dropdown.FfDropDownItem
import social.firefly.core.ui.common.dropdown.FfIconButtonDropDownMenu
import social.firefly.core.ui.common.error.GenericError
import social.firefly.core.ui.common.loading.MaxSizeLoading
import social.firefly.core.ui.common.text.MediumTextLabel
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.PostCardListItem

@Composable
internal fun ThreadScreen(
    threadStatusId: String,
    viewModel: ThreadViewModel = koinViewModel(parameters = { parametersOf(threadStatusId) }),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val threadType = viewModel.threadType.collectAsStateWithLifecycle(
        initialValue = ThreadType.TREE
    ).value

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

            when (uiState) {
                is Resource.Loading -> {
                    MaxSizeLoading()
                }
                is Resource.Error -> {
                    GenericError(
                        onRetryClicked = { threadInteractions.onRetryClicked() }
                    )
                }
                is Resource.Loaded -> {
                    ThreadList(
                        threadType = threadType,
                        statuses = uiState.data,
                        postCardDelegate = postCardDelegate,
                    )
                }
            }
        }
    }
}

@Composable
private fun ThreadList(
    threadType: ThreadType,
    statuses: ThreadPostCardCollection,
    postCardDelegate: PostCardInteractions,
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
        modifier = Modifier
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

        items(
            count = statuses.descendants.count(),
            key = { statuses.descendants[it].statusId },
        ) { index ->
            val item = statuses.descendants[index]
            PostCardListItem(
                uiState = item,
                postCardInteractions = postCardDelegate,
                showDivider = threadType != ThreadType.TREE,
            )
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
