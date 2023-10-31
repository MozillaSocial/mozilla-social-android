package org.mozilla.social.feature.account.edit

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.common.Resource
import org.mozilla.social.common.utils.toFile
import org.mozilla.social.core.designsystem.component.MoSoButton
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.component.MoSoTextField
import org.mozilla.social.core.designsystem.component.MoSoToast
import org.mozilla.social.core.designsystem.component.MoSoTopBar
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.TransparentNoTouchOverlay
import org.mozilla.social.feature.account.Header
import org.mozilla.social.feature.account.R

@Composable
internal fun EditAccountScreen(
    onDone: () -> Unit,
    viewModel: EditAccountViewModel = koinViewModel(
        parameters = {
            parametersOf(
                onDone,
            )
        }
    ),
) {
    EditAccountScreen(
        onCloseClicked = onDone,
        editAccountInteractions = viewModel,
        editAccountUiState = viewModel.editAccountUiState.collectAsState().value,
        isUploading = viewModel.isUploading.collectAsState().value,
    )

    MoSoToast(toastMessage = viewModel.errorToastMessage)
}

@Composable
fun EditAccountScreen(
    onCloseClicked: () -> Unit,
    editAccountInteractions: EditAccountInteractions,
    editAccountUiState: Resource<EditAccountUiState>,
    isUploading: Boolean,
) {
    MoSoSurface(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .systemBarsPadding()
                    .imePadding(),
            ) {
                when (editAccountUiState) {
                    is Resource.Loading -> {}
                    is Resource.Loaded -> {
                        LoadedState(
                            onCloseClicked = onCloseClicked,
                            editAccountInteractions = editAccountInteractions,
                            uiState = editAccountUiState.data,
                        )
                    }

                    is Resource.Error -> {}
                }
            }

            if (isUploading) {
                TransparentNoTouchOverlay()
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    }
}

@Composable
private fun LoadedState(
    onCloseClicked: () -> Unit,
    editAccountInteractions: EditAccountInteractions,
    uiState: EditAccountUiState,
) {
    val context = LocalContext.current

    Column {
        MoSoTopBar(
            onIconClicked = { onCloseClicked() },
            title = uiState.topBarTitle,
            rightSideContent = {
                MoSoButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .height(38.dp),
                    onClick = { editAccountInteractions.onSaveClicked() }
                ) {
                    Text(text = stringResource(id = R.string.edit_account_save_button))
                }
            },
            showDivider = false,
        )

        val avatarSelectionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let { editAccountInteractions.onNewAvatarSelected(it, it.toFile(context)) }
        }

        val headerSelectionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let { editAccountInteractions.onNewHeaderSelected(it, it.toFile(context)) }
        }

        Header(
            headerUrl = uiState.headerUrl,
            avatarUrl = uiState.avatarUrl,
            displayName = uiState.topBarTitle,
            handle = uiState.handle,
            avatarOverlay = {
                EditImageOverlay(
                    onClick = {
                        avatarSelectionLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            },
            headerOverlay = {
                EditImageOverlay(
                    onClick = {
                        headerSelectionLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }
        )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                MoSoTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = uiState.displayName,
                    onValueChange = editAccountInteractions::onDisplayNameTextChanged,
                    label = {
                        Text(text = stringResource(id = R.string.edit_account_display_name_label))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                MoSoTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = uiState.bio,
                    onValueChange = editAccountInteractions::onBioTextChanged,
                    label = {
                        Text(text = stringResource(id = R.string.edit_account_bio_label))
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.align(Alignment.End),
                text = "${uiState.bioCharacterCount}/500",
                style = MoSoTheme.typography.labelSmall,
                color = MoSoTheme.colors.textSecondary,
            )
        }
    }
}

@Composable
private fun EditImageOverlay(
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MoSoTheme.colors.actionOverlay)
            .clickable { onClick() }
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.Center),
            painter = MoSoIcons.image(),
            contentDescription = "",
        )
    }
}

@Preview
@Composable
private fun PreviewEditAccountScreen() {
    MoSoTheme {
        EditAccountScreen(
            onCloseClicked = { },
            editAccountUiState = Resource.Loaded(
                data = EditAccountUiState(
                    topBarTitle = "John",
                    headerUrl = "",
                    avatarUrl = "",
                    handle = "@john",
                    displayName = "John New",
                    bio = "I'm super cool",
                    bioCharacterCount = 20,
                )
            ),
            editAccountInteractions = object : EditAccountInteractions {},
            isUploading = false,
        )
    }
}