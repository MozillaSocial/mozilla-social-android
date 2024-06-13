package social.firefly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import social.firefly.core.datastore.AppPreferences
import social.firefly.core.datastore.AppPreferencesDatastore
import social.firefly.core.designsystem.theme.ThemeOption

class MainViewModel(
    appPreferencesDatastore: AppPreferencesDatastore,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val themeOption = appPreferencesDatastore.themeType.mapLatest {
        when (it) {
            AppPreferences.ThemeType.LIGHT -> ThemeOption.LIGHT
            AppPreferences.ThemeType.DARK -> ThemeOption.DARK
            else -> ThemeOption.SYSTEM
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ThemeOption.SYSTEM,
    )
}
