package social.firefly.core.ui.postcard.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.dropdown.FfDropDownItem
import social.firefly.core.ui.common.dropdown.FfDropdownMenu
import social.firefly.core.ui.common.loading.FfCircularProgressIndicator
import social.firefly.core.ui.postcard.MainPostCardUiState

@Composable
internal fun MetaData(
    modifier: Modifier = Modifier,
    post: MainPostCardUiState,
) {
    val context = LocalContext.current

    Row(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = post.username,
                style = FfTheme.typography.labelMedium,
            )
            Text(
                text = "${post.postTimeSince.build(context)} - @${post.accountName.build(context)}",
                style = FfTheme.typography.bodyMedium,
                color = FfTheme.colors.textSecondary,
            )
        }
        OverflowMenu(
            post = post,
        )
    }
}

@Composable
private fun OverflowMenu(
    post: MainPostCardUiState,
) {
    val overflowMenuExpanded = remember { mutableStateOf(false) }
    val context = LocalContext.current

    IconButton(
        modifier = Modifier.width(IntrinsicSize.Max),
        onClick = { overflowMenuExpanded.value = true },
    ) {
        if (post.isBeingDeleted) {
            FfCircularProgressIndicator(
                modifier =
                Modifier
                    .size(26.dp),
            )
        } else {
            Icon(painter = FfIcons.moreVertical(), contentDescription = "")
        }

        FfDropdownMenu(
            expanded = overflowMenuExpanded.value,
            onDismissRequest = {
                overflowMenuExpanded.value = false
            },
        ) {
            for (dropDownOption in post.dropDownOptions) {
                FfDropDownItem(
                    text = dropDownOption.text.build(context),
                    expanded = overflowMenuExpanded,
                    onClick = dropDownOption.onOptionClicked,
                )
            }
        }
    }
}