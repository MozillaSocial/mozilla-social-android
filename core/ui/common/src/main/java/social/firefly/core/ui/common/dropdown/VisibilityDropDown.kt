package social.firefly.core.ui.common.dropdown

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
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.model.StatusVisibility
import social.firefly.core.ui.common.R

@Composable
fun VisibilityDropDownButton(
    modifier: Modifier = Modifier,
    visibility: StatusVisibility,
    onVisibilitySelected: (StatusVisibility) -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }
    FfDropDownMenu(
        modifier = modifier,
        expanded = expanded,
        dropDownMenuContent = {
            VisibilityDropDownItem(
                type = StatusVisibility.Public,
                icon = FfIcons.public(),
                text = stringResource(id = R.string.visibility_public),
                expanded = expanded,
                onVisibilitySelected = onVisibilitySelected,
            )
            VisibilityDropDownItem(
                type = StatusVisibility.Unlisted,
                icon = FfIcons.lockOpen(),
                text = stringResource(id = R.string.visibility_unlisted),
                expanded = expanded,
                onVisibilitySelected = onVisibilitySelected,
            )
            VisibilityDropDownItem(
                type = StatusVisibility.Private,
                icon = FfIcons.materialLock(),
                text = stringResource(id = R.string.visibility_private),
                expanded = expanded,
                onVisibilitySelected = onVisibilitySelected,
            )
            VisibilityDropDownItem(
                type = StatusVisibility.Direct,
                icon = FfIcons.message(),
                text = stringResource(id = R.string.visibility_direct),
                expanded = expanded,
                onVisibilitySelected = onVisibilitySelected,
            )
        }
    ) {
        when (visibility) {
            StatusVisibility.Public ->
                ButtonContent(
                    icon = FfIcons.public(),
                    text = stringResource(id = R.string.visibility_public),
                )

            StatusVisibility.Unlisted ->
                ButtonContent(
                    icon = FfIcons.lockOpen(),
                    text = stringResource(id = R.string.visibility_unlisted),
                )

            StatusVisibility.Private ->
                ButtonContent(
                    icon = FfIcons.materialLock(),
                    text = stringResource(id = R.string.visibility_private),
                )

            StatusVisibility.Direct ->
                ButtonContent(
                    icon = FfIcons.message(),
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
            .size(FfIcons.Sizes.small)
            .align(Alignment.CenterVertically),
        tint = FfTheme.colors.iconPrimary,
    )
    Spacer(modifier = Modifier.padding(start = 8.dp))
    Text(
        modifier = Modifier.align(Alignment.CenterVertically),
        text = text,
        color = FfTheme.colors.textPrimary,
        style = FfTheme.typography.labelMedium,
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
    FfDropdownMenuItem(
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
    FfTheme(
        true,
    ) {
        VisibilityDropDownButton(
            visibility = StatusVisibility.Private,
            onVisibilitySelected = {},
        )
    }
}
