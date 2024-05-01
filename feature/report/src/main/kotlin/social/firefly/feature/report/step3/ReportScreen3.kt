package social.firefly.feature.report.step3

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import social.firefly.core.designsystem.theme.FfRadius
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.animation.ExpandingAnimation
import social.firefly.core.ui.common.appbar.FfCloseableTopAppBar
import social.firefly.core.ui.common.button.FfButton
import social.firefly.core.ui.common.button.FfButtonSecondary
import social.firefly.core.ui.common.divider.FfDivider
import social.firefly.feature.report.R

@Composable
internal fun ReportScreen3(
    onDoneClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    reportAccountId: String,
    reportAccountHandle: String,
    didUserReportAccount: Boolean,
    viewModel: ReportScreen3ViewModel =
        koinViewModel(parameters = {
            parametersOf(
                onDoneClicked,
                onCloseClicked,
                reportAccountId,
            )
        }),
) {
    val unfollowVisible by viewModel.unfollowVisible.collectAsStateWithLifecycle()
    val muteVisible by viewModel.muteVisible.collectAsStateWithLifecycle()
    val blockVisible by viewModel.blockVisible.collectAsStateWithLifecycle()
    ReportScreen3(
        reportAccountHandle = reportAccountHandle,
        didUserReportAccount = didUserReportAccount,
        unfollowVisible = unfollowVisible,
        muteVisible = muteVisible,
        blockVisible = blockVisible,
        reportInteractions = viewModel,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReportScreen3(
    reportAccountHandle: String,
    didUserReportAccount: Boolean,
    unfollowVisible: Boolean,
    muteVisible: Boolean,
    blockVisible: Boolean,
    reportInteractions: ReportScreen3Interactions,
) {
    FfSurface {
        Column(
            modifier =
            Modifier
                .fillMaxHeight()
                .systemBarsPadding(),
        ) {
            FfCloseableTopAppBar(title = stringResource(id = R.string.report_screen_title))

            TopContent(
                reportAccountHandle = reportAccountHandle,
                didUserReportAccount = didUserReportAccount,
            )

            FfDivider()

            MiddleContent(
                modifier = Modifier.weight(1f),
                reportAccountHandle = reportAccountHandle,
                didUserReportAccount = didUserReportAccount,
                unfollowVisible = unfollowVisible,
                muteVisible = muteVisible,
                blockVisible = blockVisible,
                reportInteractions = reportInteractions,
            )

            FfDivider()

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
        modifier = Modifier.padding(16.dp),
    ) {
        Text(
            text =
            stringResource(
                id =
                if (didUserReportAccount) {
                    R.string.reported_user_title
                } else {
                    R.string.limiting_user_title
                },
                "@$reportAccountHandle",
            ),
            style = FfTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text =
            stringResource(
                id =
                if (didUserReportAccount) {
                    R.string.reported_user_description
                } else {
                    R.string.limiting_user_description
                },
            ),
            style = FfTheme.typography.titleMedium,
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
        modifier =
        modifier
            .padding(16.dp),
    ) {
        Text(
            text =
            stringResource(
                id =
                if (didUserReportAccount) {
                    R.string.reported_user_options
                } else {
                    R.string.limiting_user_options
                },
            ),
            style = FfTheme.typography.titleSmall,
            fontWeight = W600,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            ExpandingAnimation(visible = unfollowVisible) {
                Column {
                    ActionableOption(
                        onClick = { reportInteractions.onUnfollowClicked() },
                        buttonText =
                        stringResource(
                            id = R.string.unfollow_user,
                            "@$reportAccountHandle",
                        ),
                        description = stringResource(id = R.string.unfollow_user_description),
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            ExpandingAnimation(visible = muteVisible) {
                Column {
                    ActionableOption(
                        onClick = { reportInteractions.onMuteClicked() },
                        buttonText =
                        stringResource(
                            id = R.string.mute_user,
                            "@$reportAccountHandle",
                        ),
                        description = stringResource(id = R.string.mute_user_description),
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            ExpandingAnimation(visible = blockVisible) {
                ActionableOption(
                    onClick = { reportInteractions.onBlockClicked() },
                    buttonText =
                    stringResource(
                        id = R.string.block_user,
                        "@$reportAccountHandle",
                    ),
                    description = stringResource(id = R.string.block_user_description),
                )
            }
        }
    }
}

@Composable
private fun BottomContent(reportInteractions: ReportScreen3Interactions) {
    FfButton(
        modifier =
        Modifier
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
        modifier =
        modifier
            .border(
                width = 1.dp,
                color = FfTheme.colors.borderPrimary,
                shape = RoundedCornerShape(FfRadius.md_8_dp),
            )
            .padding(16.dp),
    ) {
        FfButtonSecondary(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
        ) {
            Text(text = buttonText)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = description,
            style = FfTheme.typography.bodyMedium,
        )
    }
}
