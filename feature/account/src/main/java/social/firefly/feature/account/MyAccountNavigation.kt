package social.firefly.feature.account

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import social.firefly.core.navigation.BottomBarNavigationDestination

fun NavGraphBuilder.myAccountScreen() {
    composable(
        route = BottomBarNavigationDestination.MyAccount.route,
    ) {
        AccountScreen(
            accountId = null,
            windowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top),
        )
    }
}
