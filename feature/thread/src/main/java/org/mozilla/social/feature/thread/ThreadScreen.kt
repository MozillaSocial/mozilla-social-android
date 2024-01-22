package org.mozilla.social.feature.thread

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar
import org.mozilla.social.core.ui.postcard.PostCardListItem

@Composable
internal fun ThreadScreen(
    threadStatusId: String,
    viewModel: ThreadViewModel = koinViewModel(parameters = { parametersOf(threadStatusId) }),
) {
    MoSoSurface {
        Column(Modifier.systemBarsPadding()) {
            MoSoCloseableTopAppBar(title = stringResource(id = R.string.thread_screen_title))

            val items = viewModel.statuses.collectAsState(emptyList()).value

            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp),
            ) {
                items(
                    count = items.count(),
                    key = { items[it].statusId },
                ) { index ->
                    val item = items[index]
                    PostCardListItem(
                        uiState = item,
                        postCardInteractions = viewModel.postCardDelegate,
                        index = index,
                        itemCount = items.count(),
                        threadId = threadStatusId,
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onsScreenViewed()
    }
}
