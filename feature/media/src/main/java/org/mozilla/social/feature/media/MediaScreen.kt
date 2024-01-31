package org.mozilla.social.feature.media

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.mozilla.social.core.designsystem.theme.MoSoRadius
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.utils.media

@Composable
internal fun MediaScreen(
    mediaBundle: NavigationDestination.Media.MediaBundle,
) {
    MoSoSurface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        when (mediaBundle) {
            is NavigationDestination.Media.MediaBundle.Images -> {
                AsyncImage(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(RoundedCornerShape(MoSoRadius.media)),
                    model = mediaBundle.urls.first(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                )
            }
            is NavigationDestination.Media.MediaBundle.Video -> {

            }
        }
    }
}