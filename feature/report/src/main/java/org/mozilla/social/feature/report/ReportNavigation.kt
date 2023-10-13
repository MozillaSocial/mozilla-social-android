package org.mozilla.social.feature.report

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.feature.report.step1.reportScreen1
import org.mozilla.social.feature.report.step2.navigateToReportScreen2
import org.mozilla.social.feature.report.step2.reportScreen2
import org.mozilla.social.feature.report.step3.navigateToReportScreen3
import org.mozilla.social.feature.report.step3.reportScreen3

fun NavController.navigateToReport(
    navOptions: NavOptions? = null,
    reportAccountId: String,
    reportAccountHandle: String,
    reportStatusId: String? = null,
) {
    navigate(
        NavigationDestination.Report.route(
            reportAccountId = reportAccountId,
            reportAccountHandle = reportAccountHandle,
            reportStatusId = reportStatusId,
        ),
        navOptions
    )
}

fun NavGraphBuilder.reportFlow(
    onDoneClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    navController: NavController,
) {
    navigation(
        startDestination = NavigationDestination.ReportScreen1.route,
        route = NavigationDestination.Report.fullRoute,
        arguments = listOf(
            navArgument(NavigationDestination.Report.NAV_PARAM_REPORT_STATUS_ID) {
                nullable = true
            },
            navArgument(NavigationDestination.Report.NAV_PARAM_REPORT_ACCOUNT_ID) {
                nullable = true
            }
        ),
    ) {
        reportScreen1(
            onDoneClicked,
            onCloseClicked,
            onNextClicked = { reportType, bundle ->
                when (reportType) {
                    ReportType.DO_NOT_LIKE -> navController.navigateToReportScreen3()
                    else -> bundle?.let { navController.navigateToReportScreen2(it) }
                }
            }
        )
        reportScreen2(
            onReportSubmitted = {
                navController.navigateToReportScreen3()
            },
            onCloseClicked = {
                navController.popBackStack()
            }
        )
        reportScreen3(
            onDoneClicked = onDoneClicked,
        )
    }
}

