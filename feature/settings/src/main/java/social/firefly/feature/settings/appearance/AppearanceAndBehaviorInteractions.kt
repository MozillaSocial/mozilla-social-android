package social.firefly.feature.settings.appearance

import social.firefly.core.designsystem.theme.ThemeOption

interface AppearanceAndBehaviorInteractions {
    fun onThemeOptionChanged(themeOption: ThemeOption)
}