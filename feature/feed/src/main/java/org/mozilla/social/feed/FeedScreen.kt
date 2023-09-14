package org.mozilla.social.feed

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.ui.postcard.PostCardList
import org.mozilla.social.core.ui.postcard.PostCardNavigation

@Composable
fun FeedScreen(
    postCardNavigation: PostCardNavigation,
    viewModel: FeedViewModel = koinViewModel(parameters = { parametersOf(
        postCardNavigation
    ) })
) {
    PostCardList(
        feed = viewModel.feed,
        postCardInteractions = viewModel.postCardDelegate,
        reccs = viewModel.reccs.collectAsState(initial = null).value,
    )

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.postCardDelegate.errorToastMessage.collect {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }
}
