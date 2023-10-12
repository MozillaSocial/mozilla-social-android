package org.mozilla.social.feature.report.step1

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.feature.report.ReportType

internal fun NavGraphBuilder.reportScreen1(
    onDoneClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    onNextClicked: (reportType: ReportType) -> Unit,
) {
    composable(
        route = NavigationDestination.ReportScreen1.route,
    ) {
        val navController = rememberNavController()
        val reportEntry = remember(it) {
            navController.getBackStackEntry(NavigationDestination.Report.fullRoute)
        }

        val reportAccountId: String? = reportEntry.arguments?.getString(
            NavigationDestination.Report.NAV_PARAM_REPORT_ACCOUNT_ID
        )
        val reportStatusId: String? = reportEntry.arguments?.getString(
            NavigationDestination.Report.NAV_PARAM_REPORT_STATUS_ID
        )
        reportAccountId?.let {
            ReportScreen1(
                onDoneClicked,
                onCloseClicked,
                onNextClicked,
                reportAccountId = reportAccountId,
                reportStatusId = reportStatusId,
            )
        } ?: onCloseClicked()
    }
}