@file:Suppress("detekt:all")
package org.mozilla.social.feature.report.step2

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.component.MoSoButton

@Composable
internal fun ReportScreen2(
    onReportSubmitted: () -> Unit,
    viewModel: ReportScreen2ViewModel = koinViewModel(parameters = {
        parametersOf(
        )
    })
) {

}

@Composable
private fun ReportScreen2() {
    MoSoButton(onClick = { /*TODO*/ }) {

    }
}