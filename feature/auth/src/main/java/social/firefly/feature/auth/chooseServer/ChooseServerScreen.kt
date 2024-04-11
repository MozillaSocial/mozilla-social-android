package social.firefly.feature.auth.chooseServer

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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfRadius
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.navigation.navigationModule
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.appbar.FfCloseableTopAppBar
import social.firefly.core.ui.common.button.FfButton
import social.firefly.core.ui.common.loading.FfCircularProgressIndicator
import social.firefly.core.ui.common.text.FfTextField
import social.firefly.feature.auth.R

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChooseServerScreen(
    uiState: ChooseServerUiState,
    chooseServerInteractions: ChooseServerInteractions,
) {
    FfSurface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier =
            Modifier
                .systemBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState()),
        ) {
            FfCloseableTopAppBar(
                title = stringResource(id = R.string.choose_a_server_screen_title),
            )

            Column(
                modifier =
                Modifier
                    .padding(FfSpacing.md),
            ) {
                Text(
                    text = stringResource(id = R.string.choose_a_server_message),
                    style = FfTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(21.dp))
                FfTextField(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading,
                    value = uiState.serverText,
                    onValueChange = { chooseServerInteractions.onServerTextChanged(it) },
                    label = {
                        Text(text = stringResource(id = R.string.server_text_box_hint))
                    },
                    leadingIcon = {
                        Icon(
                            painter = FfIcons.globeHemisphereWest(),
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
                    Spacer(modifier = Modifier.height(FfSpacing.sm))
                    NoServerError()
                }
                Spacer(modifier = Modifier.height(21.dp))
                FfButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.nextButtonEnabled && !uiState.isLoading,
                    onClick = { chooseServerInteractions.onNextClicked() },
                ) {
                    if (uiState.isLoading) {
                        FfCircularProgressIndicator(
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
                color = FfTheme.colors.snackbarBkgError,
                shape = RoundedCornerShape(FfRadius.md_8_dp),
            ),
    ) {
        Text(
            modifier =
            Modifier
                .padding(
                    vertical = FfSpacing.sm,
                    horizontal = FfSpacing.md,
                ),
            text = stringResource(id = R.string.choose_server_error_message),
            style = FfTheme.typography.labelSmall,
            color = FfTheme.colors.snackbarTextError,
        )
    }
}

@Preview
@Composable
private fun ChooseServerScreenPreview() {
    KoinApplication(application = {
        modules(navigationModule)
    }) {
        FfTheme {
            ChooseServerScreen(
                uiState =
                ChooseServerUiState(
                    serverText = "firefly.firefly",
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
        FfTheme {
            ChooseServerScreen(
                uiState =
                ChooseServerUiState(
                    serverText = "firefly.firefly",
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
        FfTheme {
            ChooseServerScreen(
                uiState =
                ChooseServerUiState(
                    serverText = "firefly.firefly",
                    nextButtonEnabled = true,
                    loginFailed = true,
                ),
                chooseServerInteractions = object : ChooseServerInteractions {},
            )
        }
    }
}
