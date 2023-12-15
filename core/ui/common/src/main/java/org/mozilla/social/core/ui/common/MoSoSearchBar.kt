package org.mozilla.social.core.ui.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.common.text.MoSoTextField
import org.mozilla.social.core.ui.common.text.MoSoTextFieldDefaults

@OptIn(ExperimentalMaterial3Api::class)
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
    colors: SearchBarColors = MoSoSearchBarDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    MoSoTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        modifier = modifier,
        enabled = enabled,
        colors = colors.inputFieldColors,
        borderShape = RoundedCornerShape(90.dp),
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
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun colors(): SearchBarColors {
        return SearchBarDefaults.colors(
            containerColor = MoSoTheme.colors.layer2,
            dividerColor = MoSoTheme.colors.borderPrimary,
            inputFieldColors = MoSoTextFieldDefaults.colors(),
        )
    }
}