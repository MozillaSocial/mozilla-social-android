package org.mozilla.social.core.ui.common.following

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.mozilla.social.core.ui.common.R
import org.mozilla.social.core.ui.common.button.MoSoButtonContentPadding
import org.mozilla.social.core.ui.common.button.MoSoToggleButton
import org.mozilla.social.core.ui.common.button.ToggleButtonState
import org.mozilla.social.core.ui.common.text.SmallTextLabel

@Composable
fun FollowingButton(
    onButtonClicked: () -> Unit,
    isFollowing: Boolean,
    modifier: Modifier = Modifier,
) {
    MoSoToggleButton(
        modifier = modifier,
        onClick = { onButtonClicked() },
        toggleState = if (isFollowing) {
            ToggleButtonState.Secondary
        } else {
            ToggleButtonState.Primary
        },
        contentPadding = MoSoButtonContentPadding.small,
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