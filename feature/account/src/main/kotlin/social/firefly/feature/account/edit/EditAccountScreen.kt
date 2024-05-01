package social.firefly.feature.account.edit

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication
import social.firefly.common.Resource
import social.firefly.common.utils.toFile
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.navigation.navigationModule
import social.firefly.core.ui.common.FfCheckBox
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.TransparentNoTouchOverlay
import social.firefly.core.ui.common.appbar.FfCloseableTopAppBar
import social.firefly.core.ui.common.button.FfButton
import social.firefly.core.ui.common.button.FfButtonContentPadding
import social.firefly.core.ui.common.divider.FfDivider
import social.firefly.core.ui.common.error.GenericError
import social.firefly.core.ui.common.loading.MaxSizeLoading
import social.firefly.core.ui.common.text.FfTextField
import social.firefly.core.ui.common.text.SmallTextLabel
import social.firefly.feature.account.Header
import social.firefly.feature.account.R

@Composable
internal fun EditAccountScreen(viewModel: EditAccountViewModel = koinViewModel()) {
    val uiState by viewModel.editAccountUiState.collectAsStateWithLifecycle()
    val isUploading by viewModel.isUploading.collectAsStateWithLifecycle()
    EditAccountScreen(
        editAccountInteractions = viewModel,
        uiState = uiState,
        isUploading = isUploading,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountScreen(
    editAccountInteractions: EditAccountInteractions,
    uiState: Resource<EditAccountUiState>,
    isUploading: Boolean,
) {
    FfSurface(
        modifier =
        Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier =
            Modifier
                .systemBarsPadding()
                .imePadding(),
        ) {
            FfCloseableTopAppBar(
                title = (uiState as? Resource.Loaded)?.data?.topBarTitle ?: "",
                actions = {
                    if (uiState is Resource.Loaded) {
                        FfButton(
                            modifier = Modifier
                                .padding(8.dp),
                            onClick = { editAccountInteractions.onSaveClicked() },
                            contentPadding = FfButtonContentPadding.small,
                        ) {
                            SmallTextLabel(text = stringResource(id = R.string.edit_account_save_button))
                        }
                    }
                },
                showDivider = false,
            )

            when (uiState) {
                is Resource.Loading -> {
                    MaxSizeLoading()
                }

                is Resource.Loaded -> {
                    LoadedState(
                        editAccountInteractions = editAccountInteractions,
                        uiState = uiState.data,
                    )
                }

                is Resource.Error -> {
                    GenericError(
                        onRetryClicked = editAccountInteractions::onRetryClicked,
                    )
                }
            }
        }

        if (isUploading) {
            TransparentNoTouchOverlay()
            MaxSizeLoading()
        }
    }
}

@Composable
private fun LoadedState(
    editAccountInteractions: EditAccountInteractions,
    uiState: EditAccountUiState,
) {
    val context = LocalContext.current

    Column(
        modifier =
        Modifier
            .verticalScroll(rememberScrollState()),
    ) {
        val avatarSelectionLauncher =
            rememberLauncherForActivityResult(
                ActivityResultContracts.PickVisualMedia(),
            ) { uri ->
                uri?.let { editAccountInteractions.onNewAvatarSelected(it, it.toFile(context)) }
            }

        val headerSelectionLauncher =
            rememberLauncherForActivityResult(
                ActivityResultContracts.PickVisualMedia(),
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
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                        )
                    },
                )
            },
            headerOverlay = {
                EditImageOverlay(
                    onClick = {
                        headerSelectionLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                        )
                    },
                )
            },
            rightSideContent = {
                BotAndLock(
                    uiState = uiState,
                    editAccountInteractions = editAccountInteractions,
                )
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.padding(horizontal = 8.dp),
        ) {
            FfTextField(
                modifier =
                Modifier
                    .fillMaxWidth(),
                value = uiState.displayName,
                onValueChange = editAccountInteractions::onDisplayNameTextChanged,
                label = {
                    Text(text = stringResource(id = R.string.edit_account_display_name_label))
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            FfTextField(
                modifier =
                Modifier
                    .fillMaxWidth(),
                value = uiState.bio,
                onValueChange = editAccountInteractions::onBioTextChanged,
                label = {
                    Text(text = stringResource(id = R.string.edit_account_bio_label))
                },
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.align(Alignment.End),
                text = "${uiState.bioCharacterCount}/500",
                style = FfTheme.typography.labelSmall,
                color = FfTheme.colors.textSecondary,
            )

            Metadata(
                editAccountInteractions = editAccountInteractions,
                fields = uiState.fields,
            )
        }
    }
}

@Composable
private fun EditImageOverlay(onClick: () -> Unit) {
    Box(
        modifier =
        Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(FfTheme.colors.actionOverlay)
            .clickable { onClick() },
    ) {
        Icon(
            modifier =
            Modifier
                .align(Alignment.Center),
            painter = FfIcons.image(),
            contentDescription = "",
            tint = FfTheme.colors.actionSecondary,
        )
    }
}

@Composable
private fun BotAndLock(
    editAccountInteractions: EditAccountInteractions,
    uiState: EditAccountUiState,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FfCheckBox(
            checked = uiState.lockChecked,
            onCheckedChange = { editAccountInteractions.onLockClicked() },
        )
        Icon(
            painter = FfIcons.lock(),
            contentDescription = "",
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(id = R.string.edit_account_lock),
            style = FfTheme.typography.labelSmall,
            color = FfTheme.colors.textSecondary,
        )

        Spacer(modifier = Modifier.width(16.dp))

        FfCheckBox(
            checked = uiState.botChecked,
            onCheckedChange = { editAccountInteractions.onBotClicked() },
        )
        Icon(
            painter = FfIcons.robot(),
            contentDescription = "",
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(id = R.string.edit_account_bot),
            style = FfTheme.typography.labelSmall,
            color = FfTheme.colors.textSecondary,
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
private fun Metadata(
    editAccountInteractions: EditAccountInteractions,
    fields: List<EditAccountUiStateField>,
) {
    Column {
        Text(
            text = stringResource(id = R.string.edit_account_metadata_title),
            style = FfTheme.typography.bodyMedium,
            fontWeight = W700,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.edit_account_metadata_description),
            style = FfTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(16.dp))
        fields.forEachIndexed { index, field ->
            FfTextField(
                modifier = Modifier.fillMaxWidth(),
                value = field.label,
                onValueChange = { editAccountInteractions.onLabelTextChanged(index, it) },
                label = {
                    Text(text = stringResource(id = R.string.edit_account_label_hint))
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            FfTextField(
                modifier = Modifier.fillMaxWidth(),
                value = field.content,
                onValueChange = { editAccountInteractions.onContentTextChanged(index, it) },
                label = {
                    Text(text = stringResource(id = R.string.edit_account_content_hint))
                },
            )
            if (index < fields.size - 1) {
                Spacer(modifier = Modifier.height(16.dp))
                FfDivider()
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
private fun PreviewEditAccountScreen() {
    KoinApplication(application = {
        modules(navigationModule)
    }) {
        FfTheme {
            EditAccountScreen(
                uiState =
                Resource.Loaded(
                    data =
                    EditAccountUiState(
                        topBarTitle = "John",
                        headerUrl = "",
                        avatarUrl = "",
                        handle = "@john",
                        displayName = "John New",
                        bio = "I'm super cool",
                        bioCharacterCount = 20,
                        lockChecked = false,
                        botChecked = false,
                        fields = listOf(),
                    ),
                ),
                editAccountInteractions = object : EditAccountInteractions {},
                isUploading = false,
            )
        }
    }
}
