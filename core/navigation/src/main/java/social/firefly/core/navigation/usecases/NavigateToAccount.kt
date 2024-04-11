package social.firefly.core.navigation.usecases

import social.firefly.core.navigation.NavigationDestination

class NavigateToAccount(private val navigateTo: NavigateTo) {
    operator fun invoke(id: String) {
        navigateTo(NavigationDestination.Account(id))
    }
}