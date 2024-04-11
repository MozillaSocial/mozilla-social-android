package social.firefly.core.ui.common.search

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.R
import social.firefly.core.ui.common.text.MediumTextBody
import social.firefly.core.ui.common.text.FfTextFieldDefaults
import social.firefly.core.ui.common.utils.PreviewTheme

/**
 * Search bar text field that is based on the material 3 text field, but
 * with some changes to padding and minimum height.  Also adds default
 * placeholder and leading icon.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FfSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.shape,
    colors: SearchBarColors = SearchBarTextFieldDefaults.colors(),
    borderShape: Shape = RoundedCornerShape(90.dp),
) {
    // If color is not provided via the text style, use content color as a default
    val textColor = textStyle.color.takeOrElse {
        colors.textColor
    }
    val mergedTextStyle = textStyle.merge(FfTheme.typography.bodyMedium.merge(color = textColor))

    CompositionLocalProvider(LocalTextSelectionColors provides colors.selectionColors) {
        BasicTextField(
            value = query,
            modifier = modifier
                .height(42.dp)
                .border(
                    width = 1.dp,
                    color =
                    if (isError) {
                        FfTheme.colors.borderWarning
                    } else {
                        colors.borderColor
                    },
                    shape = borderShape,
                ),
            onValueChange = onQueryChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            cursorBrush = SolidColor(colors.cursorColor),
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(query)
                },
            ),
            interactionSource = interactionSource,
            singleLine = true,
            maxLines = 1,
            minLines = 1,
            decorationBox = @Composable { innerTextField ->
                // places leading icon, text field with label and placeholder, trailing icon
                TextFieldDefaults.DecorationBox(
                    value = query,
                    visualTransformation = visualTransformation,
                    innerTextField = innerTextField,
                    placeholder = {
                        MediumTextBody(
                            text = stringResource(id = R.string.search),
                            color = FfTheme.colors.textSecondary,
                        )
                    },
                    label = label,
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = FfIcons.magnifyingGlass(),
                            contentDescription = stringResource(id = R.string.search),
                            tint = FfTheme.colors.iconSecondary,
                        )
                    },
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    supportingText = supportingText,
                    shape = shape,
                    singleLine = true,
                    enabled = enabled,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = colors.inputFieldColors,
                    contentPadding = PaddingValues(0.dp)
                )
            }
        )
    }
}

internal object SearchBarTextFieldDefaults {
    @Composable
    fun colors(): SearchBarColors =
        SearchBarColors(
            cursorColor = FfTheme.colors.textPrimary,
            textColor = FfTheme.colors.textPrimary,
            selectionColors = LocalTextSelectionColors.current,
            borderColor = FfTheme.colors.borderInputEnabled,
            inputFieldColors = FfTextFieldDefaults.colors(),
        )
}

data class SearchBarColors(
    val cursorColor: Color,
    val textColor: Color,
    val selectionColors: TextSelectionColors,
    val borderColor: Color,
    val inputFieldColors: TextFieldColors,
)

@Preview
@Composable
private fun FfSearchBarPreview() {
    PreviewTheme {
        FfSearchBar(
            query = "",
            onQueryChange = {},
            onSearch = {},
            trailingIcon = {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = FfIcons.x(),
                    contentDescription = null,
                    tint = FfTheme.colors.iconSecondary,
                )
            },
        )
    }
}

@Preview
@Composable
private fun FfSearchBarPreview2() {
    PreviewTheme {
        FfSearchBar(
            query = "test",
            onQueryChange = {},
            onSearch = {},
            trailingIcon = {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = FfIcons.x(),
                    contentDescription = null,
                    tint = FfTheme.colors.iconSecondary,
                )
            },
        )
    }
}
