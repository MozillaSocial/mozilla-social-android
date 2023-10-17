@file:Suppress("detekt:all")
package org.mozilla.social.feature.report.step3

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.component.MoSoButton
import org.mozilla.social.core.designsystem.component.MoSoButtonSecondary
import org.mozilla.social.core.designsystem.component.MoSoCircularProgressIndicator
import org.mozilla.social.core.designsystem.component.MoSoDivider
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.component.MoSoToast
import org.mozilla.social.core.designsystem.component.MoSoTopBar
import org.mozilla.social.core.designsystem.theme.MoSoRadius
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.animation.ExpandingAnimation
import org.mozilla.social.feature.report.R

@Composable
internal fun ReportScreen3(
    onDoneClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    reportAccountId: String,
    reportAccountHandle: String,
    didUserReportAccount: Boolean,
    viewModel: ReportScreen3ViewModel = koinViewModel(parameters = {
        parametersOf(
            onDoneClicked,
            onCloseClicked,
            reportAccountId
        )
    })
) {
    ReportScreen3(
        reportAccountHandle = reportAccountHandle,
        didUserReportAccount = didUserReportAccount,
        unfollowVisible = viewModel.unfollowVisible.collectAsState().value,
        muteVisible = viewModel.muteVisible.collectAsState().value,
        blockVisible = viewModel.blockVisible.collectAsState().value,
        reportInteractions = viewModel,
    )

    MoSoToast(toastMessage = viewModel.errorToastMessage)
}

@Composable
private fun ReportScreen3(
    reportAccountHandle: String,
    didUserReportAccount: Boolean,
    unfollowVisible: Boolean,
    muteVisible: Boolean,
    blockVisible: Boolean,
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

            TopContent(
                reportAccountHandle = reportAccountHandle,
                didUserReportAccount = didUserReportAccount,
            )

            MoSoDivider()

            MiddleContent(
                modifier = Modifier.weight(1f),
                reportAccountHandle = reportAccountHandle,
                didUserReportAccount = didUserReportAccount,
                unfollowVisible = unfollowVisible,
                muteVisible = muteVisible,
                blockVisible = blockVisible,
                reportInteractions = reportInteractions,
            )

            MoSoDivider()

            BottomContent(
                reportInteractions = reportInteractions,
            )
        }
    }
}

@Composable
private fun TopContent(
    reportAccountHandle: String,
    didUserReportAccount: Boolean,
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(id = if (didUserReportAccount) {
                R.string.reported_user_title
            } else {
                R.string.limiting_user_title
            }, "@$reportAccountHandle"),
            style = MoSoTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(
                id = if (didUserReportAccount) {
                    R.string.reported_user_description
                } else {
                    R.string.limiting_user_description
                }
            ),
            style = MoSoTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun MiddleContent(
    modifier: Modifier = Modifier,
    reportAccountHandle: String,
    didUserReportAccount: Boolean,
    unfollowVisible: Boolean,
    muteVisible: Boolean,
    blockVisible: Boolean,
    reportInteractions: ReportScreen3Interactions,
) {
    Column(
        modifier = modifier
            .padding(16.dp),
    ) {
        Text(
            text = stringResource(
                id = if (didUserReportAccount) {
                    R.string.reported_user_options
                } else {
                    R.string.limiting_user_options
                }
            ),
            style = MoSoTheme.typography.titleSmall,
            fontWeight = W600
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            ExpandingAnimation(visible = unfollowVisible) {
                Column {
                    ActionableOption(
                        onClick = { reportInteractions.onUnfollowClicked() },
                        buttonText = stringResource(id = R.string.unfollow_user, "@$reportAccountHandle"),
                        description = stringResource(id = R.string.unfollow_user_description)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            ExpandingAnimation(visible = muteVisible) {
                Column {
                    ActionableOption(
                        onClick = { reportInteractions.onMuteClicked() },
                        buttonText = stringResource(id = R.string.mute_user, "@$reportAccountHandle"),
                        description = stringResource(id = R.string.mute_user_description)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            ExpandingAnimation(visible = blockVisible) {
                ActionableOption(
                    onClick = { reportInteractions.onBlockClicked() },
                    buttonText = stringResource(id = R.string.block_user, "@$reportAccountHandle"),
                    description = stringResource(id = R.string.block_user_description)
                )
            }
        }
    }
}

@Composable
private fun BottomContent(
    reportInteractions: ReportScreen3Interactions,
) {
    MoSoButton(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        onClick = { reportInteractions.onDoneClicked() },
    ) {
        Text(text = stringResource(id = R.string.done_button))
    }
}

@Composable
private fun ActionableOption(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    buttonText: String,
    description: String,
) {
    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MoSoTheme.colors.borderPrimary,
                shape = RoundedCornerShape(MoSoRadius.md)
            )
            .padding(16.dp),
    ) {
        MoSoButtonSecondary(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
        ) {
            Text(text = buttonText)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = description,
            style = MoSoTheme.typography.bodyMedium,
        )
    }
}