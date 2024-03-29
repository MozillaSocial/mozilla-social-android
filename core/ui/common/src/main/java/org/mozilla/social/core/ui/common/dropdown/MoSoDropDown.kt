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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import kotlinx.coroutines.delay
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.designsystem.utils.NoRipple
import org.mozilla.social.core.ui.common.utils.PreviewTheme

@Suppress("MagicNumber")
@Composable
fun MoSoDropDownMenu(
    modifier: Modifier = Modifier,
    expanded: MutableState<Boolean>,
    properties: PopupProperties = PopupProperties(focusable = false),
    dropDownMenuContent: @Composable ColumnScope.() -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    var canExpand by remember {
        mutableStateOf(true)
    }

    // slight delay for allowing the menu to expand after being close.
    // necessary for making closing the menu by clicking the button not instantly re-open the menu.
    LaunchedEffect(key1 = expanded.value) {
        canExpand = if (!expanded.value) {
            delay(200)
            true
        } else {
            false
        }
    }

    Box(
        modifier = modifier,
    ) {
        NoRipple {
            Row(
                modifier = Modifier
                    .clickable { if (canExpand) expanded.value = true },
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
        }
        MoSoDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            },
            properties = properties,
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
        MoSoDropDownMenu(
            expanded = mutableStateOf(false),
            dropDownMenuContent = {},
        ) {
            Text(text = "Choose")
        }
    }
}