package org.mozilla.social.feature.report.step3

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.serialization.json.Json
import org.mozilla.social.core.navigation.ReportNavigationDestination
import org.mozilla.social.feature.report.ReportDataBundle

internal fun NavGraphBuilder.reportScreen3(
    onDoneClicked: () -> Unit,
    onCloseClicked: () -> Unit,
) {
    composable(
        route = ReportNavigationDestination.ReportScreen3.fullRoute,
        arguments =
            listOf(
                navArgument(ReportNavigationDestination.ReportScreen3.NAV_PARAM_BUNDLE) {
                    nullable = false
                },
            ),
    ) {
        val bundle: String =
            it.arguments?.getString(
                ReportNavigationDestination.ReportScreen3.NAV_PARAM_BUNDLE,
            )!!

        val deserializedBundle: ReportDataBundle.ReportDataBundleForScreen3 = Json.decodeFromString(bundle)

        ReportScreen3(
            onDoneClicked = onDoneClicked,
            onCloseClicked = onCloseClicked,
            reportAccountId = deserializedBundle.reportAccountId,
            reportAccountHandle = deserializedBundle.reportAccountHandle,
            didUserReportAccount = deserializedBundle.didUserReportAccount,
        )
    }
}
