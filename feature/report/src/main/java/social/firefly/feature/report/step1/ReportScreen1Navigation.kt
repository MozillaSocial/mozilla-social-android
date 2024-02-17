package social.firefly.feature.report.step1

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.ReportNavigationDestination
import social.firefly.feature.report.ReportDataBundle

internal fun NavGraphBuilder.reportScreen1(
    onCloseClicked: () -> Unit,
    onNextClicked: (bundle: ReportDataBundle) -> Unit,
) {
    composable(
        route = ReportNavigationDestination.ReportScreen1.route,
    ) {
        val reportAccountId: String? =
            it.arguments?.getString(
                NavigationDestination.Report.NAV_PARAM_REPORT_ACCOUNT_ID,
            )
        val reportAccountHandle: String? =
            it.arguments?.getString(
                NavigationDestination.Report.NAV_PARAM_REPORT_ACCOUNT_HANDLE,
            )
        val reportStatusId: String? =
            it.arguments?.getString(
                NavigationDestination.Report.NAV_PARAM_REPORT_STATUS_ID,
            )
        if (reportAccountId == null || reportAccountHandle == null) {
            onCloseClicked()
            return@composable
        }

        ReportScreen1(
            onCloseClicked,
            onNextClicked,
            reportAccountId = reportAccountId,
            reportAccountHandle = reportAccountHandle,
            reportStatusId = reportStatusId,
        )
    }
}
