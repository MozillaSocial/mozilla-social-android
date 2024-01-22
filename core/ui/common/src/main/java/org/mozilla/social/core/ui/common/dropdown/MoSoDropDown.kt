package org.mozilla.social.core.ui.common.dropdown

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.common.utils.PreviewTheme

@Composable
fun MoSoDropDown(
    modifier: Modifier = Modifier,
    expanded: MutableState<Boolean>,
    dropDownMenuContent: @Composable ColumnScope.() -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .clickable { expanded.value = true },
        ) {
            content()
            Spacer(modifier = Modifier.padding(start = 8.dp))
            Icon(
                modifier = Modifier
                    .size(MoSoIcons.Sizes.small)
                    .align(Alignment.CenterVertically),
                painter = MoSoIcons.caretDown(),
                contentDescription = null,
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

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun MoSoDropDownPreview() {
    PreviewTheme {
        MoSoDropDown(
            expanded = mutableStateOf(false),
            dropDownMenuContent = {},
        ) {
            Text(text = "Choose")
        }
    }
}