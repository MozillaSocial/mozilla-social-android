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
    isFollowing: Boolean,
    modifier: Modifier = Modifier,
) {
    FfToggleButton(
        modifier = modifier,
        onClick = { onButtonClicked() },
        toggleState = if (isFollowing) {
            ToggleButtonState.Secondary
        } else {
            ToggleButtonState.Primary
        },
        contentPadding = FfButtonContentPadding.small,
    ) {
        SmallTextLabel(
            text = if (isFollowing) {
                stringResource(id = R.string.following_button)
            } else {
                stringResource(id = R.string.follow_button)
            }
        )
    }
}