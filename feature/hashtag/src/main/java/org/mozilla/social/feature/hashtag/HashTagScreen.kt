package org.mozilla.social.feature.hashtag

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import org.mozilla.social.core.designsystem.component.MoSoTopBar
import org.mozilla.social.core.ui.postcard.PostCardNavigation

@Composable
internal fun HashTagRoute(
    hashTag: String,
    onCloseClicked: () -> Unit,
    postCardNavigation: PostCardNavigation,
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