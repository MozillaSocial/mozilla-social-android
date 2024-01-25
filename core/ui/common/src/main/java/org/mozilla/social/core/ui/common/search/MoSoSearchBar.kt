package org.mozilla.social.core.ui.common.search

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.common.text.MoSoTextFieldDefaults
import org.mozilla.social.core.ui.common.utils.PreviewTheme

@Composable
fun MoSoSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null,
    colors: SearchBarColors = SearchBarTextFieldDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    SearchBarTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        modifier = modifier
            .height(32.dp),
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource,
        label = label,
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(query)
            },
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        singleLine = true,
    )
}

object MoSoSearchBarDefaults {
    @Composable
    fun colors(): MoSoSearchBarColors {
        return MoSoSearchBarColors(
            inputFieldColors = MoSoTextFieldDefaults.colors(),
            searchBarColors = SearchBarTextFieldDefaults.colors(),
        )
    }
}

data class MoSoSearchBarColors(
    val inputFieldColors: TextFieldColors,
    val searchBarColors: SearchBarColors,
)

@Preview
@Composable
private fun MoSoSearchBarPreview() {
    PreviewTheme {
        MoSoSearchBar(
            query = "",
            onQueryChange = {},
            onSearch = {},
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = MoSoIcons.magnifyingGlass(),
                    contentDescription = null,
                    tint = MoSoTheme.colors.iconSecondary,
                )
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = MoSoIcons.x(),
                    contentDescription = null,
                    tint = MoSoTheme.colors.iconSecondary,
                )
            },
            placeholder = {
                Text(text = "Search")
            }
        )
    }
}
