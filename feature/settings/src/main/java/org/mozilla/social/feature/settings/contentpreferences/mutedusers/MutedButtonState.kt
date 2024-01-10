package org.mozilla.social.feature.settings.contentpreferences.mutedusers

import androidx.annotation.StringRes
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.ui.common.account.toggleablelist.ToggleableButtonState
import org.mozilla.social.core.ui.common.button.MoSoButtonPrimaryDefaults
import org.mozilla.social.core.ui.common.button.MoSoButtonSecondaryDefaults
import org.mozilla.social.core.ui.common.button.MoSoButtonTheme
import org.mozilla.social.feature.settings.R

/**
 * Muted button state is an implementation of [ToggleableButtonState] which encapsulates the
 * different states a user can be in, and what the text on the button should be for those states
 */
sealed class MutedButtonState(
    @get:StringRes override val text: Int,
    override val confirmationText: StringFactory? = null,
    override val theme: MoSoButtonTheme,
) : ToggleableButtonState {
    data object Muted : MutedButtonState(
        text = R.string.unmute,
        theme = MoSoButtonSecondaryDefaults,
    ) {
        override val confirmationText: StringFactory? = null
    }

    data class Unmuted(override val confirmationText: StringFactory?) : MutedButtonState(
        text = R.string.mute,
        theme = MoSoButtonPrimaryDefaults,
    )
}