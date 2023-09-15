package org.mozilla.social.feature.thread

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.component.MoSoTopBar
import org.mozilla.social.core.ui.postcard.PostCardList
import org.mozilla.social.core.ui.postcard.PostCardNavigation

@Composable
fun ThreadScreen(
    threadStatusId: String,
    onCloseClicked: () -> Unit,
    postCardNavigation: PostCardNavigation,
    viewModel: ThreadViewModel = koinViewModel(parameters = { parametersOf(
        threadStatusId,
        postCardNavigation,
    ) })
) {
    Column {
        MoSoTopBar(
            title = "Thread",
            onCloseClicked = onCloseClicked,
        )
        Divider()

        PostCardList(
            items = viewModel.statuses.collectAsState(emptyList()).value,
            postCardInteractions = viewModel.postCardDelegate
        )
    }
}
