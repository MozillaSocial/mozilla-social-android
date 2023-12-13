package org.mozilla.social.feature.settings.contentpreferences.mutedusers

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.account.toggleablelist.ToggleableAccountList
import org.mozilla.social.core.ui.common.account.toggleablelist.ToggleableButtonState
import org.mozilla.social.core.ui.common.button.MoSoButtonPrimaryDefaults
import org.mozilla.social.core.ui.common.button.MoSoButtonSecondaryDefaults
import org.mozilla.social.core.ui.common.button.MoSoButtonTheme
import org.mozilla.social.feature.settings.R
import org.mozilla.social.feature.settings.ui.SettingsColumn

@Composable
fun MutedUsersSettingsScreen(viewModel: MutedUsersSettingsViewModel = koinViewModel()) {
    MoSoSurface {
        SettingsColumn(title = stringResource(id = R.string.muted_users_title)) {
            ToggleableAccountList(
                pagingData = viewModel.mutes,
                onButtonClicked = viewModel::onButtonClicked
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

/**
 * Muted button state is an implementation of [ToggleableButtonState] which encapsulates the
 * different states a user can be in, and what the text on the button should be for those states
 */
sealed class MutedButtonState(
    @get:StringRes override val text: Int,
    override val confirmationText: StringFactory? = null,
    override val theme: MoSoButtonTheme,
) : ToggleableButtonState {
    class Muted(confirmationText: StringFactory?) : MutedButtonState(
        text = R.string.unmute,
        confirmationText = confirmationText,
        theme = MoSoButtonSecondaryDefaults,
    )

    data object Unmuted : MutedButtonState(
        text = R.string.mute,
        theme = MoSoButtonPrimaryDefaults,
    )
}