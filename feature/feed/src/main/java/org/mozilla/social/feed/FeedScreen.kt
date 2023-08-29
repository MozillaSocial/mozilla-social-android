package org.mozilla.social.feed

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.ui.postcard.PostCardList

@Composable
fun FeedScreen(
    onReplyClicked: (String) -> Unit,
    viewModel: FeedViewModel = koinViewModel(parameters = { parametersOf(onReplyClicked) })
) {
    PostCardList(
        feed = viewModel.feed,
        postCardInteractions = viewModel.postCardDelegate
    )

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.postCardDelegate.errorToastMessage.collect {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }
}
