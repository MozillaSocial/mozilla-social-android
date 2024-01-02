package org.mozilla.social.core.ui.common.hashtag.quickview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoSpacing
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.common.button.MoSoButton
import org.mozilla.social.core.ui.common.following.FollowingButton
import org.mozilla.social.core.ui.common.text.LargeTextBody
import org.mozilla.social.core.ui.common.text.MediumTextBody
import org.mozilla.social.core.ui.common.utils.PreviewTheme

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
                .background(MoSoTheme.colors.layer2),
            ) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
                painter = MoSoIcons.hash(),
                contentDescription = "",
            )
        }


        Spacer(modifier = Modifier.width(MoSoSpacing.sm))

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
                    isFollowing = uiState.isFollowing
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
                isFollowing = true
            ),
            onButtonClicked = {}
        )
    }
}