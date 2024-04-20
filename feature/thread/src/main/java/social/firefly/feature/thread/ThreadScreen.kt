package social.firefly.feature.thread

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.appbar.FfCloseableTopAppBar
import social.firefly.core.ui.postcard.PostCardDelegate
import social.firefly.core.ui.postcard.PostCardListItem
import social.firefly.core.ui.postcard.PostCardUiState

@Composable
internal fun ThreadScreen(
    threadStatusId: String,
    viewModel: ThreadViewModel = koinViewModel(parameters = { parametersOf(threadStatusId) }),
) {
    val statuses = viewModel.statuses.collectAsStateWithLifecycle(initialValue = emptyList()).value
    val threadType = viewModel.threadType.collectAsStateWithLifecycle(
        initialValue = ThreadType.TREE
    ).value

    ThreadScreen(
        threadType = threadType,
        threadStatusId = threadStatusId,
        statuses = statuses,
        postCardDelegate = viewModel.postCardDelegate,
    )

    LaunchedEffect(Unit) {
        viewModel.onsScreenViewed()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThreadScreen(
    threadType: ThreadType,
    threadStatusId: String,
    statuses: List<PostCardUiState>,
    postCardDelegate: PostCardDelegate,
) {
    FfSurface {
        Column(Modifier.systemBarsPadding()) {
            FfCloseableTopAppBar(
                title = stringResource(id = R.string.thread_screen_title),
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
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
            )

            LazyColumn(
                Modifier
                    .fillMaxSize(),
            ) {
                items(
                    count = statuses.count(),
                    key = { statuses[it].statusId },
                ) { index ->
                    val item = statuses[index]
                    PostCardListItem(
                        uiState = item,
                        postCardInteractions = postCardDelegate,
                        index = index,
                        itemCount = statuses.count(),
                        threadId = threadStatusId,
                        showDividers = false,
                    )
                }
            }
        }
    }
}
