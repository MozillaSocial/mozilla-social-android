package social.firefly.core.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import social.firefly.core.designsystem.theme.FfTheme

@Composable
fun FfCard(
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape, // TODO@design-system
    colors: CardColors = FfCardDefaults.colors(),
    elevation: CardElevation = CardDefaults.cardElevation(), // TODO@design-system
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        content = content,
    )
}

object FfCardDefaults {
    @Composable
    fun colors(): CardColors {
        return CardDefaults.cardColors(
            containerColor = FfTheme.colors.layer2,
            contentColor = FfTheme.colors.textPrimary,
            disabledContainerColor = FfTheme.colors.layer2,
            disabledContentColor = FfTheme.colors.textPrimary,
        )
    }
}
