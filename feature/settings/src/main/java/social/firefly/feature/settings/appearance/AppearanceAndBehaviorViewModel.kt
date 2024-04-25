package social.firefly.feature.settings.appearance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import social.firefly.core.datastore.AppPreferences.ThemeType
import social.firefly.core.datastore.AppPreferencesDatastore

class AppearanceAndBehaviorViewModel(
    private val appPreferencesDatastore: AppPreferencesDatastore,
) : ViewModel(), AppearanceAndBehaviorInteractions {

    @OptIn(ExperimentalCoroutinesApi::class)
    val themeOption = appPreferencesDatastore.themeType.mapLatest {
        when (it) {
            ThemeType.LIGHT -> ThemeOption.LIGHT
            ThemeType.DARK -> ThemeOption.DARK
            else -> ThemeOption.SYSTEM
        }
    }

    override fun onThemeOptionChanged(themeOption: ThemeOption) {
        viewModelScope.launch {
            appPreferencesDatastore.saveThemeType(
                when (themeOption) {
                    ThemeOption.SYSTEM -> ThemeType.SYSTEM
                    ThemeOption.LIGHT -> ThemeType.LIGHT
                    ThemeOption.DARK -> ThemeType.DARK
                }
            )
        }
    }
}