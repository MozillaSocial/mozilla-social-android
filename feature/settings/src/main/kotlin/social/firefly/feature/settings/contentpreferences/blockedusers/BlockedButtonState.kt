package social.firefly.feature.settings.contentpreferences.blockedusers

import androidx.annotation.StringRes
import social.firefly.common.utils.StringFactory
import social.firefly.core.ui.common.account.toggleablelist.ToggleableButtonState
import social.firefly.core.ui.common.button.FfButtonPrimaryDefaults
import social.firefly.core.ui.common.button.FfButtonSecondaryDefaults
import social.firefly.core.ui.common.button.FfButtonTheme
import social.firefly.feature.settings.R

/**
 * Blocked button state is an implementation of [ToggleableButtonState] which encapsulates the
 * different states a user can be in, and what the text on the button should be for those states
 */
sealed class BlockedButtonState(
    @get:StringRes override val text: Int,
    override val confirmationText: StringFactory? = null,
    override val theme: FfButtonTheme,
) : ToggleableButtonState {
    data object Blocked : BlockedButtonState(
        text = R.string.unblock,
        theme = FfButtonSecondaryDefaults,
    ) {
        override val confirmationText: StringFactory? = null
    }

    data class Unblocked(override val confirmationText: StringFactory?) : BlockedButtonState(
        text = R.string.block,
        theme = FfButtonPrimaryDefaults,
    )
}