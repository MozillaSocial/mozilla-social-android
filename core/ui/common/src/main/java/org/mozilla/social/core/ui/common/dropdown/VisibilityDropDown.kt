package org.mozilla.social.core.ui.common.dropdown

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.model.StatusVisibility
import org.mozilla.social.core.ui.common.R

@Composable
fun VisibilityDropDownButton(
    modifier: Modifier = Modifier,
    visibility: StatusVisibility,
    onVisibilitySelected: (StatusVisibility) -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }
    MoSoDropDownMenu(
        modifier = modifier,
        expanded = expanded,
        dropDownMenuContent = {
            VisibilityDropDownItem(
                type = StatusVisibility.Public,
                icon = MoSoIcons.public(),
                text = stringResource(id = R.string.visibility_public),
                expanded = expanded,
                onVisibilitySelected = onVisibilitySelected,
            )
            VisibilityDropDownItem(
                type = StatusVisibility.Unlisted,
                icon = MoSoIcons.lockOpen(),
                text = stringResource(id = R.string.visibility_unlisted),
                expanded = expanded,
                onVisibilitySelected = onVisibilitySelected,
            )
            VisibilityDropDownItem(
                type = StatusVisibility.Private,
                icon = MoSoIcons.materialLock(),
                text = stringResource(id = R.string.visibility_private),
                expanded = expanded,
                onVisibilitySelected = onVisibilitySelected,
            )
            VisibilityDropDownItem(
                type = StatusVisibility.Direct,
                icon = MoSoIcons.message(),
                text = stringResource(id = R.string.visibility_direct),
                expanded = expanded,
                onVisibilitySelected = onVisibilitySelected,
            )
        }
    ) {
        when (visibility) {
            StatusVisibility.Public ->
                ButtonContent(
                    icon = MoSoIcons.public(),
                    text = stringResource(id = R.string.visibility_public),
                )

            StatusVisibility.Unlisted ->
                ButtonContent(
                    icon = MoSoIcons.lockOpen(),
                    text = stringResource(id = R.string.visibility_unlisted),
                )

            StatusVisibility.Private ->
                ButtonContent(
                    icon = MoSoIcons.materialLock(),
                    text = stringResource(id = R.string.visibility_private),
                )

            StatusVisibility.Direct ->
                ButtonContent(
                    icon = MoSoIcons.message(),
                    text = stringResource(id = R.string.visibility_direct),
                )
        }
    }
}

@Composable
private fun RowScope.ButtonContent(
    icon: Painter,
    text: String,
) {
    Icon(
        icon,
        "",
        modifier = Modifier
            .size(MoSoIcons.Sizes.small)
            .align(Alignment.CenterVertically),
        tint = MoSoTheme.colors.iconPrimary,
    )
    Spacer(modifier = Modifier.padding(start = 8.dp))
    Text(
        modifier = Modifier.align(Alignment.CenterVertically),
        text = text,
        color = MoSoTheme.colors.textPrimary,
        style = MoSoTheme.typography.labelMedium,
    )
}

@Composable
private fun VisibilityDropDownItem(
    type: StatusVisibility,
    icon: Painter,
    text: String,
    expanded: MutableState<Boolean>,
    onVisibilitySelected: (StatusVisibility) -> Unit,
    contentDescription: String = "",
) {
    MoSoDropdownMenuItem(
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
        },
    )
}

@Preview
@Composable
private fun VisibilityDropDownPreview() {
    MoSoTheme(
        true,
    ) {
        VisibilityDropDownButton(
            visibility = StatusVisibility.Private,
            onVisibilitySelected = {},
        )
    }
}
