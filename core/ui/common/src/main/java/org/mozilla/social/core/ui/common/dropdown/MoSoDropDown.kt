package org.mozilla.social.core.ui.common.dropdown

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoTheme

@Composable
fun MoSoDropDown(
    modifier: Modifier = Modifier,
    dropDownMenuContent: ColumnScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }

    Box {
        Box(
            modifier = modifier
                .clickable { expanded.value = true },
        ) {
            content()
            Spacer(modifier = Modifier.padding(start = 8.dp))
            Icon(
                MoSoIcons.caretDown(),
                "",
                Modifier.size(MoSoIcons.Sizes.small),
                tint = MoSoTheme.colors.iconPrimary,
            )
        }
        MoSoDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            },
        ) {
            dropDownMenuContent()
        }
    }
}