package org.mozilla.social.core.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import org.mozilla.social.core.designsystem.theme.MoSoTheme

@Composable
fun MoSoCard(
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape, // TODO@design-system
    colors: CardColors = MoSoCardDefaults.colors(),
    elevation: CardElevation = CardDefaults.cardElevation(), // TODO@design-system
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        content = content
    )
}

object MoSoCardDefaults {
    @Composable
    fun colors(): CardColors {
        return CardDefaults.cardColors(
            containerColor = MoSoTheme.colors.layer2,
            contentColor = MoSoTheme.colors.textPrimary,
            disabledContainerColor = MoSoTheme.colors.layer2,
            disabledContentColor = MoSoTheme.colors.textPrimary
        )
    }
}