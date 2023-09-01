package org.mozilla.social.feature.report

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val REPORT_STATUS_ID = "reportStatusId"
const val REPORT_ACCOUNT_ID = "reportAccountId"
const val REPORT_ROUTE = "report"
const val REPORT_FULL_ROUTE = "$REPORT_ROUTE?$REPORT_STATUS_ID={$REPORT_STATUS_ID}&$REPORT_ACCOUNT_ID={$REPORT_ACCOUNT_ID}"

fun NavController.navigateToReport(
    navOptions: NavOptions? = null,
    reportAccountId: String,
    reportStatusId: String? = null,
) {
    when {
        reportStatusId != null -> navigate("$REPORT_ROUTE?$REPORT_STATUS_ID=$reportStatusId&$REPORT_ACCOUNT_ID=$reportAccountId", navOptions)
        else -> navigate("$REPORT_ROUTE?$REPORT_ACCOUNT_ID=$reportAccountId", navOptions)
    }
}

fun NavGraphBuilder.reportScreen(
    onReported: () -> Unit,
    onCloseClicked: () -> Unit,
) {
    composable(
        route = REPORT_FULL_ROUTE,
        arguments = listOf(
            navArgument(REPORT_STATUS_ID) {
                nullable = true
            },
            navArgument(REPORT_ACCOUNT_ID) {
                nullable = true
            }
        )
    ) {
        val reportAccountId: String? = it.arguments?.getString(REPORT_ACCOUNT_ID)
        val reportStatusId: String? = it.arguments?.getString(REPORT_STATUS_ID)
        reportAccountId?.let {
            ReportRoute(
                onReported,
                onCloseClicked,
                reportAccountId = reportAccountId,
                reportStatusId = reportStatusId,
            )
        } ?: onCloseClicked()
    }
}