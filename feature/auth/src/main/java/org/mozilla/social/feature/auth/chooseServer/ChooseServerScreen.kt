package org.mozilla.social.feature.auth.chooseServer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoRadius
import org.mozilla.social.core.designsystem.theme.MoSoSpacing
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar
import org.mozilla.social.core.ui.common.button.MoSoButton
import org.mozilla.social.core.ui.common.loading.MoSoCircularProgressIndicator
import org.mozilla.social.core.ui.common.text.MoSoTextField
import org.mozilla.social.feature.auth.R

@Composable
internal fun ChooseServerScreen(viewModel: ChooseServerViewModel = koinViewModel()) {
    val uiState: ChooseServerUiState by viewModel.uiState.collectAsStateWithLifecycle()
    ChooseServerScreen(
        uiState = uiState,
        chooseServerInteractions = viewModel,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@Composable
private fun ChooseServerScreen(
    uiState: ChooseServerUiState,
    chooseServerInteractions: ChooseServerInteractions,
) {
    MoSoSurface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier =
                Modifier
                    .systemBarsPadding()
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
        ) {
            MoSoCloseableTopAppBar(
                title = stringResource(id = R.string.choose_a_server_screen_title),
            )

            Column(
                modifier =
                    Modifier
                        .padding(MoSoSpacing.md),
            ) {
                Text(
                    text = stringResource(id = R.string.choose_a_server_message),
                    style = MoSoTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(21.dp))
                MoSoTextField(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading,
                    value = uiState.serverText,
                    onValueChange = { chooseServerInteractions.onServerTextChanged(it) },
                    label = {
                        Text(text = stringResource(id = R.string.server_text_box_hint))
                    },
                    leadingIcon = {
                        Icon(
                            painter = MoSoIcons.globeHemisphereWest(),
                            contentDescription = null,
                        )
                    },
                    isError = uiState.loginFailed,
                    singleLine = true,
                    keyboardActions =
                        KeyboardActions(
                            onDone = {
                                chooseServerInteractions.onNextClicked()
                            },
                        ),
                )
                if (uiState.loginFailed) {
                    Spacer(modifier = Modifier.height(MoSoSpacing.sm))
                    NoServerError()
                }
                Spacer(modifier = Modifier.height(21.dp))
                MoSoButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.nextButtonEnabled && !uiState.isLoading,
                    onClick = { chooseServerInteractions.onNextClicked() },
                ) {
                    if (uiState.isLoading) {
                        MoSoCircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                        )
                    } else {
                        Text(text = stringResource(id = R.string.choose_server_next_button))
                    }
                }
            }
        }
    }
}

@Composable
private fun NoServerError(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    color = MoSoTheme.colors.snackbarBkgError,
                    shape = RoundedCornerShape(MoSoRadius.md_8_dp),
                ),
    ) {
        Text(
            modifier =
                Modifier
                    .padding(
                        vertical = MoSoSpacing.sm,
                        horizontal = MoSoSpacing.md,
                    ),
            text = stringResource(id = R.string.choose_server_error_message),
            style = MoSoTheme.typography.labelSmall,
            color = MoSoTheme.colors.snackbarTextError,
        )
    }
}

@Preview
@Composable
private fun ChooseServerScreenPreview() {
    KoinApplication(application = {
        modules(navigationModule)
    }) {
        MoSoTheme {
            ChooseServerScreen(
                uiState =
                    ChooseServerUiState(
                        serverText = "mozilla.social",
                        nextButtonEnabled = true,
                    ),
                chooseServerInteractions = object : ChooseServerInteractions {},
            )
        }
    }
}

@Preview
@Composable
private fun ChooseServerScreenLoadingPreview() {
    KoinApplication(application = {
        modules(navigationModule)
    }) {
        MoSoTheme {
            ChooseServerScreen(
                uiState =
                    ChooseServerUiState(
                        serverText = "mozilla.social",
                        nextButtonEnabled = true,
                        isLoading = true,
                    ),
                chooseServerInteractions = object : ChooseServerInteractions {},
            )
        }
    }
}

@Preview
@Composable
private fun ChooseServerScreenErrorPreview() {
    KoinApplication(application = {
        modules(navigationModule)
    }) {
        MoSoTheme {
            ChooseServerScreen(
                uiState =
                    ChooseServerUiState(
                        serverText = "mozilla.social",
                        nextButtonEnabled = true,
                        loginFailed = true,
                    ),
                chooseServerInteractions = object : ChooseServerInteractions {},
            )
        }
    }
}
