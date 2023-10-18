package org.mozilla.social.feature.report

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.navOptions
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
            onCloseClicked = navController::popBackStack,
            onNextClicked = { bundle ->
                when (bundle) {
                    is ReportDataBundle.ReportDataBundleForScreen2 -> {
                        navController.navigateToReportScreen2(Json.encodeToString(bundle))
                    }
                    is ReportDataBundle.ReportDataBundleForScreen3 -> {
                        navController.navigateToReportScreen3(Json.encodeToString(bundle))
                    }
                }
            }
        )
        reportScreen2(
            onReportSubmitted = {
                navController.navigateToReportScreen3(
                    Json.encodeToString(it),
                    navOptions {
                        // once a report is submitted, there is no going back.
                        // remove the previous report screens from the back stack
                        popUpTo(NavigationDestination.Report.fullRoute) {
                            inclusive = true
                        }
                    }
                )
            },
            onCloseClicked = navController::popBackStack
        )
        reportScreen3(
            onDoneClicked = {
                navController.popBackStack(NavigationDestination.Report.fullRoute, true)
            },
            onCloseClicked = navController::popBackStack,
        )
    }
}

