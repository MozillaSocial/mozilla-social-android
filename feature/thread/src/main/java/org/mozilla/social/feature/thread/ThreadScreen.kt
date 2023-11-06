package org.mozilla.social.feature.thread

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.navigation.usecases.PopNavBackstack
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar
import org.mozilla.social.core.ui.postcard.PostCardList


@Composable
internal fun ThreadScreen(
    threadStatusId: String,
    popBackstack: PopNavBackstack = koinInject(),
    viewModel: ThreadViewModel = koinViewModel(parameters = { parametersOf(threadStatusId) })
) {
    MoSoSurface {
        Column(Modifier.systemBarsPadding()) {
            MoSoCloseableTopAppBar(
                title = stringResource(id = R.string.thread_screen_title),
                onIconClicked = { popBackstack() },
            )

            PostCardList(
                items = viewModel.statuses.collectAsState(emptyList()).value,
                errorToastMessage = viewModel.postCardDelegate.errorToastMessage,
                postCardInteractions = viewModel.postCardDelegate,
                threadId = threadStatusId,
            )
        }
    }
}
