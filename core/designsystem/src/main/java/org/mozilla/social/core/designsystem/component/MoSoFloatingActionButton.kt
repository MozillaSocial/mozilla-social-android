package org.mozilla.social.core.designsystem.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoTheme

@Composable
fun MoSoFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MoSoFloatingActionButtonDefaults.shape,
    containerColor: Color = MoSoFloatingActionButtonDefaults.containerColor,
    contentColor: Color = MoSoFloatingActionButtonDefaults.contentColor,
    elevation: FloatingActionButtonElevation = MoSoFloatingActionButtonDefaults.elevation,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = elevation,
        interactionSource = interactionSource,
        content = content
    )
}

object MoSoFloatingActionButtonDefaults {

    /** Default shape for a floating action button. */
    val shape: Shape @Composable get() = CircleShape


    /** Default container color for a floating action button. */
    val containerColor: Color @Composable get() = MoSoTheme.colors.actionPrimary


    /** Default container color for a floating action button. */
    val contentColor: Color @Composable get() = MoSoTheme.colors.textActionPrimary

    val elevation: FloatingActionButtonElevation @Composable get() = FloatingActionButtonDefaults.elevation()
}

