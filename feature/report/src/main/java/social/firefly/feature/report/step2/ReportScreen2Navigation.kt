package social.firefly.feature.report.step2

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.serialization.json.Json
import social.firefly.core.navigation.ReportNavigationDestination
import social.firefly.feature.report.ReportDataBundle

internal fun NavGraphBuilder.reportScreen2(
    onReportSubmitted: (bundle: ReportDataBundle.ReportDataBundleForScreen3) -> Unit,
    onCloseClicked: () -> Unit,
) {
    composable(
        route = ReportNavigationDestination.ReportScreen2.fullRoute,
        arguments =
            listOf(
                navArgument(ReportNavigationDestination.ReportScreen2.NAV_PARAM_BUNDLE) {
                    nullable = false
                },
            ),
    ) {
        val bundle: String =
            it.arguments?.getString(
                ReportNavigationDestination.ReportScreen2.NAV_PARAM_BUNDLE,
            )!!

        val deserializedBundle: ReportDataBundle.ReportDataBundleForScreen2 = Json.decodeFromString(bundle)

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
