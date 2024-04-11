package social.firefly.core.ui.common.hashtag.quickview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import social.firefly.common.utils.StringFactory
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.following.FollowStatus
import social.firefly.core.ui.common.following.FollowingButton
import social.firefly.core.ui.common.text.LargeTextBody
import social.firefly.core.ui.common.text.MediumTextBody
import social.firefly.core.ui.common.utils.PreviewTheme

@Composable
fun HashTagQuickView(
    uiState: HashTagQuickViewUiState,
    modifier: Modifier = Modifier,
    onButtonClicked: () -> Unit,
) {
    Row(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(FfTheme.colors.layer2),
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
                painter = FfIcons.hash(),
                contentDescription = "",
            )
        }


        Spacer(modifier = Modifier.width(FfSpacing.sm))

        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                LargeTextBody(
                    text = "#${uiState.name}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                val context = LocalContext.current
                MediumTextBody(
                    text = uiState.details.build(context),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Box(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                FollowingButton(
                    onButtonClicked = onButtonClicked,
                    followStatus = uiState.followStatus
                )
            }
        }
    }
}

@Preview
@Composable
private fun HashTagQuickViewPreview() {
    PreviewTheme {
        HashTagQuickView(
            uiState = HashTagQuickViewUiState(
                name = "linux",
                details = StringFactory.literal("18 in past 5 days"),
                followStatus = FollowStatus.NOT_FOLLOWING
            ),
            onButtonClicked = {}
        )
    }
}