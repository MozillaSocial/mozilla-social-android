package org.mozilla.social.feature.report.step2

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.serialization.json.Json
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.feature.report.ReportDataBundle

internal fun NavController.navigateToReportScreen2(
    bundle: String,
    navOptions: NavOptions? = null,
) {
    navigate(NavigationDestination.ReportScreen2.route(bundle), navOptions)
}

internal fun NavGraphBuilder.reportScreen2(
    onReportSubmitted: () -> Unit,
    onCloseClicked: () -> Unit,
) {
    composable(
        route = NavigationDestination.ReportScreen2.fullRoute,
        arguments = listOf(
            navArgument(NavigationDestination.ReportScreen2.NAV_PARAM_BUNDLE) {
                nullable = false
            }
        )
    ) {
        val bundle: String = it.arguments?.getString(
            NavigationDestination.ReportScreen2.NAV_PARAM_BUNDLE
        )!!

        val deserializedBundle: ReportDataBundle = Json.decodeFromString(bundle)

        ReportScreen2(
            onCloseClicked = onCloseClicked,
            onReportSubmitted = onReportSubmitted,
            reportAccountId = deserializedBundle.reportAccountId,
            reportAccountHandle = deserializedBundle.reportAccountHandle,
            reportStatusId = deserializedBundle.reportStatusId,
            reportType = deserializedBundle.reportType,
            checkedInstanceRules = deserializedBundle.checkedInstanceRules,
            additionalText = deserializedBundle.additionalText,
            sendToExternalServer = deserializedBundle.sendToExternalServer,
        )
    }
}