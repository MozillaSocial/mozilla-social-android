@file:Suppress("detekt:all")
package org.mozilla.social.feature.report.step3

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.component.MoSoButton
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.component.MoSoTopBar
import org.mozilla.social.feature.report.R

@Composable
internal fun ReportScreen3(
    onDoneClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    viewModel: ReportScreen3ViewModel = koinViewModel(parameters = {
        parametersOf(
            onDoneClicked,
            onCloseClicked,
        )
    })
) {
    ReportScreen3(
        reportInteractions = viewModel,
    )
}

@Composable
private fun ReportScreen3(
    reportInteractions: ReportScreen3Interactions,
) {
    MoSoSurface {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            MoSoTopBar(
                title = stringResource(id = R.string.report_screen_title),
                onIconClicked = { reportInteractions.onCloseClicked() }
            )
        }
    }
}