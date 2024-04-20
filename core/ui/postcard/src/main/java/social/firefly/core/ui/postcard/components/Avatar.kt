package social.firefly.core.ui.postcard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.postcard.MainPostCardUiState
import social.firefly.core.ui.postcard.PostCardInteractions

@Composable
internal fun Avatar(
    modifier: Modifier = Modifier,
    post: MainPostCardUiState,
    postCardInteractions: PostCardInteractions,
) {
    AsyncImage(
        modifier =
        modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(FfTheme.colors.layer2)
            .clickable { postCardInteractions.onAccountImageClicked(post.accountId) },
        model = post.profilePictureUrl,
        contentDescription = null,
    )
}