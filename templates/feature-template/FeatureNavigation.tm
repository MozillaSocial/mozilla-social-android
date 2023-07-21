package ${PACKAGE_NAME}

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ${FEATURE_NAME}_ROUTE = "${FEATURE_NAME}-route"

fun NavController.navigateTo${FEATURE_NAME}(navOptions: NavOptions? = null) {
    this.navigate(${FEATURE_NAME}_ROUTE, navOptions)
}

fun NavGraphBuilder.${FEATURE_NAME_LOWERCASE}Screen() {
    composable(route = ${FEATURE_NAME}_ROUTE) {
    }
}