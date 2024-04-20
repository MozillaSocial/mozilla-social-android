package social.firefly.core.ui.postcard.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.postcard.TopRowIconType
import social.firefly.core.ui.postcard.TopRowMetaDataUiState

@Composable
internal fun TopRowMetaData(
    modifier: Modifier = Modifier,
    topRowMetaDataUiState: TopRowMetaDataUiState,
) {
    Row(
        modifier = modifier,
    ) {
        Icon(
            modifier =
            Modifier
                .size(20.dp)
                .align(Alignment.CenterVertically),
            painter =
            when (topRowMetaDataUiState.iconType) {
                TopRowIconType.BOOSTED -> FfIcons.boost()
                TopRowIconType.REPLY -> FfIcons.chatBubbles()
            },
            contentDescription = "",
        )
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = topRowMetaDataUiState.text.build(LocalContext.current),
            style = FfTheme.typography.bodyMedium,
        )
    }
}