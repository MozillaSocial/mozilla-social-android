package org.mozilla.social.feature.report.step3

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.mozilla.social.core.navigation.NavigationDestination

internal fun NavController.navigateToReportScreen3(
    navOptions: NavOptions? = null,
) {
    navigate(NavigationDestination.ReportScreen3.route, navOptions)
}

internal fun NavGraphBuilder.reportScreen3(
    onDoneClicked: () -> Unit
) {
    composable(
        route = NavigationDestination.ReportScreen3.route,
    ) {
        ReportScreen3(
            onDoneClicked = onDoneClicked,
        )
    }
}