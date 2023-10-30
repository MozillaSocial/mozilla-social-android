package org.mozilla.social.feature.account.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.common.Resource
import org.mozilla.social.core.designsystem.component.MoSoButton
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.component.MoSoTextField
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.appbar.MoSoCloseableTopAppBar
import org.mozilla.social.feature.account.Header
import org.mozilla.social.feature.account.R

@Composable
internal fun EditAccountScreen(
    viewModel: EditAccountViewModel = koinViewModel(),
) {
    EditAccountScreen(
        editAccountInteractions = viewModel,
        editAccountUiState = viewModel.editAccountUiState.collectAsState().value,
    )
}

@Composable
fun EditAccountScreen(
    editAccountInteractions: EditAccountInteractions,
    editAccountUiState: Resource<EditAccountUiState>,
) {
    MoSoSurface(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .systemBarsPadding()
                .imePadding(),
        ) {
            when (editAccountUiState) {
                is Resource.Loading -> {}
                is Resource.Loaded -> {
                    LoadedState(
                        editAccountInteractions = editAccountInteractions,
                        uiState = editAccountUiState.data,
                    )
                }

                is Resource.Error -> {}
            }
        }
    }
}

@Composable
private fun LoadedState(
    editAccountInteractions: EditAccountInteractions,
    uiState: EditAccountUiState,
) {
    Column {
        MoSoCloseableTopAppBar(
            title = uiState.topBarTitle,
            actions = {
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
        Header(
            headerUrl = uiState.headerUrl,
            avatarUrl = uiState.avatarUrl,
            displayName = uiState.topBarTitle,
            handle = uiState.handle,
        ) {
            //TODO bot and lock
        }

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

@Preview
@Composable
private fun PreviewEditAccountScreen() {
    MoSoTheme {
        EditAccountScreen(
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
            editAccountInteractions = object : EditAccountInteractions {}
        )
    }
}