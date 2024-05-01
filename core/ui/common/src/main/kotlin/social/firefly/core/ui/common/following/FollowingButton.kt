package social.firefly.core.ui.common.following

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import social.firefly.core.ui.common.R
import social.firefly.core.ui.common.button.FfButtonContentPadding
import social.firefly.core.ui.common.button.FfToggleButton
import social.firefly.core.ui.common.button.ToggleButtonState
import social.firefly.core.ui.common.text.SmallTextLabel

@Composable
fun FollowingButton(
    onButtonClicked: () -> Unit,
    followStatus: FollowStatus,
    modifier: Modifier = Modifier,
) {
    FfToggleButton(
        modifier = modifier,
        onClick = { onButtonClicked() },
        toggleState = when (followStatus) {
            FollowStatus.FOLLOWING -> ToggleButtonState.Secondary
            FollowStatus.PENDING_REQUEST -> ToggleButtonState.Secondary
            FollowStatus.NOT_FOLLOWING -> ToggleButtonState.Primary
        },
        contentPadding = FfButtonContentPadding.small,
    ) {
        SmallTextLabel(
            text = when (followStatus) {
                FollowStatus.FOLLOWING -> stringResource(id = R.string.following_button)
                FollowStatus.PENDING_REQUEST -> stringResource(id = R.string.pending_follow_button)
                FollowStatus.NOT_FOLLOWING -> stringResource(id = R.string.follow_button)
            }
        )
    }
}