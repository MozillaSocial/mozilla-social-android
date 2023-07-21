package org.mozilla.social.core.designsystem.utils

import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.drawscope.ContentDrawScope

/**
 * Used for compose click indications.  Typically the click indication is a ripple effect.
 * This is for removing the ripple effect.
 * Can be applied like so:
 *
       CompositionLocalProvider(
            LocalIndication provides NoIndication
        ) {}
 */
object NoIndication : Indication {
    private object NoIndicationInstance : IndicationInstance {
        override fun ContentDrawScope.drawIndication() {
            drawContent()
        }
    }

    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        return NoIndicationInstance
    }
}