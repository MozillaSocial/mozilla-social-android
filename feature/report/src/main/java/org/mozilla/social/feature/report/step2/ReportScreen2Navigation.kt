package org.mozilla.social.feature.report.step2

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.mozilla.social.core.navigation.NavigationDestination

internal fun NavController.navigateToReportScreen2(
    navOptions: NavOptions? = null,
) {
    navigate(NavigationDestination.ReportScreen2.route, navOptions)
}

internal fun NavGraphBuilder.reportScreen2(
    onReportSubmitted: () -> Unit
) {
    composable(
        route = NavigationDestination.ReportScreen2.route,
    ) {
        ReportScreen2(
            onReportSubmitted = onReportSubmitted,
        )
    }
}