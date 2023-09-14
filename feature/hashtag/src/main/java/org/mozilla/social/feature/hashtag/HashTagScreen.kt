package org.mozilla.social.feature.hashtag

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.component.MoSoTopBar
import org.mozilla.social.core.ui.postcard.PostCardNavigation

@Composable
internal fun HashTagRoute(
    hashTag: String,
    onCloseClicked: () -> Unit,
    postCardNavigation: PostCardNavigation,
    viewModel: HashTagViewModel = koinViewModel(parameters = { parametersOf(
        hashTag,
        postCardNavigation,
    ) } )
) {
    HashTagScreen(
        hashTag = hashTag,
        onCloseClicked = onCloseClicked,
    )
}

@Composable
private fun HashTagScreen(
    hashTag: String,
    onCloseClicked: () -> Unit,
) {
    Column {
        MoSoTopBar(
            title = "#$hashTag",
            onCloseClicked = onCloseClicked,
        )
    }
}