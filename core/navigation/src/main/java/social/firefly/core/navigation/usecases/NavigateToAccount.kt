package social.firefly.core.navigation.usecases

import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo

class NavigateToAccount(private val navigateTo: NavigateTo) {
    operator fun invoke(id: String) {
        navigateTo(NavigationDestination.Account(id))
    }
}