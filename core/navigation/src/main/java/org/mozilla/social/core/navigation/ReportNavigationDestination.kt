package org.mozilla.social.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

sealed class ReportNavigationDestination(
    val route: String,
) {
    data object ReportScreen1 : ReportNavigationDestination(
        route = "report1",
    )

    data object ReportScreen2 : ReportNavigationDestination(
        route = "report2",
    ) {
        fun NavController.navigateToReportScreen2(
            bundle: String,
            navOptions: NavOptions? = null,
        ) {
            navigate(route(bundle), navOptions)
        }

        const val NAV_PARAM_BUNDLE = "reportDataBundle"
        val fullRoute =
            "$route?" +
                "$NAV_PARAM_BUNDLE={$NAV_PARAM_BUNDLE}"

        fun route(bundle: String): String =
            "$route?" +
                "$NAV_PARAM_BUNDLE=$bundle"
    }

    data object ReportScreen3 : ReportNavigationDestination(
        route = "report3",
    ) {
        fun NavController.navigateToReportScreen3(
            bundle: String,
            navOptions: NavOptions? = null,
        ) {
            navigate(route(bundle), navOptions)
        }

        const val NAV_PARAM_BUNDLE = "reportDataBundle"
        val fullRoute =
            "$route?" +
                "$NAV_PARAM_BUNDLE={$NAV_PARAM_BUNDLE}"

        fun route(bundle: String): String =
            "$route?" +
                "$NAV_PARAM_BUNDLE=$bundle"
    }
}
