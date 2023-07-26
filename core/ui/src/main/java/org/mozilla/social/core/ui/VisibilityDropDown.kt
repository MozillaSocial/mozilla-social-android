package org.mozilla.social.core.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.model.entity.StatusVisibility

@Composable
fun VisibilityDropDownButton(
    visibility: StatusVisibility,
    onVisibilitySelected: (StatusVisibility) -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { expanded.value = true }
    ) {
        when (visibility) {
            StatusVisibility.Public -> ButtonContent(icon = Icons.Default.Public, text = "Public")
            StatusVisibility.Unlisted -> ButtonContent(icon = Icons.Default.LockOpen, text = "Unlisted")
            StatusVisibility.Private -> ButtonContent(icon = Icons.Default.Lock, text = "Private")
            StatusVisibility.Direct -> ButtonContent(icon = Icons.Default.Message, text = "Direct")
        }
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Icon(Icons.Default.ArrowDropDown, "")
    }

    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = {
            expanded.value = false
        }
    ) {
        DropDownItem(
            type = StatusVisibility.Public,
            icon = Icons.Default.Public,
            text = "Public",
            expanded = expanded,
            onVisibilitySelected = onVisibilitySelected
        )
        DropDownItem(
            type = StatusVisibility.Unlisted,
            icon = Icons.Default.LockOpen,
            text = "Unlisted",
            expanded = expanded,
            onVisibilitySelected = onVisibilitySelected
        )
        DropDownItem(
            type = StatusVisibility.Private,
            icon = Icons.Default.Lock,
            text = "Private",
            expanded = expanded,
            onVisibilitySelected = onVisibilitySelected
        )
        DropDownItem(
            type = StatusVisibility.Direct,
            icon = Icons.Default.Message,
            text = "Direct",
            expanded = expanded,
            onVisibilitySelected = onVisibilitySelected
        )
    }
}

@Composable
private fun ButtonContent(
    icon: ImageVector,
    text: String,
) {
    Icon(icon, "")
    Spacer(modifier = Modifier.padding(start = 8.dp))
    Text(text = text)
}

@Composable
private fun DropDownItem(
    type: StatusVisibility,
    icon: ImageVector,
    text: String,
    expanded: MutableState<Boolean>,
    onVisibilitySelected: (StatusVisibility) -> Unit,
    contentDescription: String = "",
) {
    DropdownMenuItem(
        text = {
            Row {
                Icon(icon, contentDescription)
                Spacer(modifier = Modifier.padding(start = 8.dp))
                Text(text = text)
            }
        },
        onClick = {
            onVisibilitySelected(type)
            expanded.value = false
        }
    )
}

@Preview
@Composable
private fun VisibilityDropDownPreview() {
    MozillaSocialTheme {
        VisibilityDropDownButton(
            visibility = StatusVisibility.Private,
            onVisibilitySelected = {}
        )
    }
}