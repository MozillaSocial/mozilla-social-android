package org.mozilla.social.feature.report

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.mozilla.social.core.navigation.NavigationDestination

fun NavController.navigateToReport(
    navOptions: NavOptions? = null,
    reportAccountId: String,
    reportStatusId: String? = null,
) {
    navigate(NavigationDestination.Report.route(reportAccountId, reportStatusId), navOptions)
}

fun NavGraphBuilder.reportScreen(
    onReported: () -> Unit,
    onCloseClicked: () -> Unit,
) {
    composable(
        route = NavigationDestination.Report.fullRoute,
        arguments = listOf(
            navArgument(NavigationDestination.Report.NAV_PARAM_REPORT_STATUS_ID) {
                nullable = true
            },
            navArgument(NavigationDestination.Report.NAV_PARAM_REPORT_ACCOUNT_ID) {
                nullable = true
            }
        )
    ) {
        val reportAccountId: String? = it.arguments?.getString(NavigationDestination.Report.NAV_PARAM_REPORT_ACCOUNT_ID)
        val reportStatusId: String? = it.arguments?.getString(NavigationDestination.Report.NAV_PARAM_REPORT_STATUS_ID)
        reportAccountId?.let {
            ReportScreen(
                onReported,
                onCloseClicked,
                reportAccountId = reportAccountId,
                reportStatusId = reportStatusId,
            )
        } ?: onCloseClicked()
    }
}