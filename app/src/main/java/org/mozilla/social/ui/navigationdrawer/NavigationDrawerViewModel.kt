package org.mozilla.social.ui.navigationdrawer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.mozilla.social.core.domain.Logout

class NavigationDrawerViewModel(
    private val logout: Logout,
    private val onLoggedOut: () -> Unit,
): ViewModel() {

    fun onLogoutClicked() {
        viewModelScope.launch {
            logout()
            onLoggedOut()
        }
    }
}