package social.firefly.feature.thread

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.appbar.FfCloseableTopAppBar
import social.firefly.core.ui.postcard.PostCardListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ThreadScreen(
    threadStatusId: String,
    viewModel: ThreadViewModel = koinViewModel(parameters = { parametersOf(threadStatusId) }),
) {
    FfSurface {
        Column(Modifier.systemBarsPadding()) {
            FfCloseableTopAppBar(title = stringResource(id = R.string.thread_screen_title))

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
