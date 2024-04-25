package social.firefly.feature.settings.appearance

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.dropdown.FfDropDownItem
import social.firefly.core.ui.common.dropdown.FfDropDownMenu
import social.firefly.core.ui.common.text.SmallTextLabel
import social.firefly.feature.settings.R
import social.firefly.feature.settings.ui.SettingsColumn
import social.firefly.feature.settings.ui.SettingsGroup
import social.firefly.feature.settings.ui.SettingsSelection

@Composable
internal fun AppearanceAndBehaviorScreen(
    viewModel: AppearanceAndBehaviorViewModel = koinViewModel()
) {
    val themeOption = viewModel.themeOption.collectAsStateWithLifecycle(initialValue = ThemeOption.SYSTEM).value

    AppearanceAndBehaviorScreen(
        themeOption = themeOption,
        appearanceAndBehaviorInteractions = viewModel,
    )
}

@Composable
private fun AppearanceAndBehaviorScreen(
    themeOption: ThemeOption,
    appearanceAndBehaviorInteractions: AppearanceAndBehaviorInteractions,
) {
    FfSurface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
    ) {
        SettingsColumn(title = stringResource(id = R.string.appearance_and_behavior_title)) {
            SettingsGroup(name = R.string.appearance_group_title) {
                SettingsSelection(
                    title = stringResource(id = R.string.change_theme_title),
                ) {
                    val themeMenuExpanded = remember { mutableStateOf(false) }

                    FfDropDownMenu(
                        expanded = themeMenuExpanded,
                        dropDownMenuContent = {
                            FfDropDownItem(
                                text = stringResource(id = R.string.theme_option_system),
                                expanded = themeMenuExpanded,
                                onClick = { appearanceAndBehaviorInteractions.onThemeOptionChanged(ThemeOption.SYSTEM) }
                            )
                            FfDropDownItem(
                                text = stringResource(id = R.string.theme_option_light),
                                expanded = themeMenuExpanded,
                                onClick = { appearanceAndBehaviorInteractions.onThemeOptionChanged(ThemeOption.LIGHT) }
                            )
                            FfDropDownItem(
                                text = stringResource(id = R.string.theme_option_dark),
                                expanded = themeMenuExpanded,
                                onClick = { appearanceAndBehaviorInteractions.onThemeOptionChanged(ThemeOption.DARK) }
                            )
                        }
                    ) {
                        SmallTextLabel(
                            text = when (themeOption) {
                                ThemeOption.SYSTEM -> stringResource(id = R.string.theme_option_system)
                                ThemeOption.LIGHT -> stringResource(id = R.string.theme_option_light)
                                ThemeOption.DARK -> stringResource(id = R.string.theme_option_dark)
                            }
                        )
                    }
                }
            }
        }
    }
}