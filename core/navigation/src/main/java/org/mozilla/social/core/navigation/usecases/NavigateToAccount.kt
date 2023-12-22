package org.mozilla.social.core.navigation.usecases

import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo

class NavigateToAccount(private val navigateTo: NavigateTo) {
    operator fun invoke(id: String) {
        navigateTo(NavigationDestination.Account(id))
    }
}