package org.mozilla.social.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.mozilla.social.core.domain.IsSignedInFlow
import org.mozilla.social.core.navigation.MoSoNavigationRelay
import org.mozilla.social.core.navigation.NavDestination
import org.mozilla.social.core.navigation.NavigateTo

class NavigationViewModel(
    private val moSoNavigationRelay: MoSoNavigationRelay,
    private val navigateTo: NavigateTo,
    private val isSignedInFlow: IsSignedInFlow,
) : ViewModel() {

    val navigationEvents: SharedFlow<NavDestination>
        get() = moSoNavigationRelay.navigationEvents

    init {
        viewModelScope.launch {
            isSignedInFlow().collect {
                if (!it) {
                    navigateTo(NavDestination.Login)
                } else {
                    navigateTo(NavDestination.Feed)
                }
            }
        }
    }
}
