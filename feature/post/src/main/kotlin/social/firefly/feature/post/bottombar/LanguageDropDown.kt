package social.firefly.feature.post.bottombar

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.dropdown.FfDropDownMenu
import social.firefly.core.ui.common.dropdown.FfDropdownMenuItem
import social.firefly.core.ui.common.text.MediumTextLabel

@Composable
internal fun LanguageDropDown(
    modifier: Modifier = Modifier,
    bottomBarState: BottomBarState,
    onLanguageClicked: (code: String) -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }

    FfDropDownMenu(
        modifier = modifier,
        expanded = expanded,
        dropDownMenuContent = {
            bottomBarState.availableLocales.forEach { locale ->
                FfDropdownMenuItem(
                    text = {
                        Text(
                            text = locale.displayName
                        )
                    },
                    onClick = {
                        expanded.value = false
                        onLanguageClicked(locale.code)
                    }
                )
            }
        }
    ) {
        Icon(
            painter = FfIcons.globe(),
            contentDescription = null,
            modifier = Modifier
                .size(FfIcons.Sizes.small)
                .align(Alignment.CenterVertically),
            tint = FfTheme.colors.iconPrimary,
        )
        Spacer(modifier = Modifier.padding(start = 8.dp))
        MediumTextLabel(
            text = bottomBarState.language
        )
    }
}