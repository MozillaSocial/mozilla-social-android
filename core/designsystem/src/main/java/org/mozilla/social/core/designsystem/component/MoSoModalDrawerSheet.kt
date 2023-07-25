package org.mozilla.social.core.designsystem.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MoSoModalDrawerSheet(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    ModalDrawerSheet(modifier = modifier, content = content)
}