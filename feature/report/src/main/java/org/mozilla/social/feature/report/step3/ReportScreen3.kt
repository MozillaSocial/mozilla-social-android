@file:Suppress
package org.mozilla.social.feature.report.step3

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.component.MoSoButton

@Composable
internal fun ReportScreen3(
    onDoneClicked: () -> Unit,
    viewModel: ReportScreen3ViewModel = koinViewModel(parameters = {
        parametersOf(
        )
    })
) {

}

@Composable
private fun ReportScreen3() {
    MoSoButton(onClick = { /*TODO*/ }) {

    }
}