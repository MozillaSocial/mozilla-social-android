package org.mozilla.social.feature.account.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.common.Resource
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.component.MoSoTextField
import org.mozilla.social.core.designsystem.component.MoSoTopBar
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
    )
}

@Composable
fun EditAccountScreen(
    onCloseClicked: () -> Unit,
    editAccountInteractions: EditAccountInteractions,
    editAccountUiState: Resource<EditAccountUiState>,
) {
    MoSoSurface(
        modifier = Modifier.fillMaxSize()
    ) {
        when (editAccountUiState) {
            is Resource.Loading -> {}
            is Resource.Loaded -> {
                LoadedState(
                    onCloseClicked = onCloseClicked,
                    editAccountInteractions = editAccountInteractions,
                    editAccountUiState = editAccountUiState.data,
                )
            }
            is Resource.Error -> {}
        }
    }
}

@Composable
private fun LoadedState(
    onCloseClicked: () -> Unit,
    editAccountInteractions: EditAccountInteractions,
    editAccountUiState: EditAccountUiState,
) {
    Column {
        MoSoTopBar(onIconClicked = { onCloseClicked() })
        Header(
            headerUrl = editAccountUiState.headerUrl,
            avatarUrl = editAccountUiState.avatarUrl
        ) {
            //TODO bot and lock
        }
        MoSoTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = editAccountUiState.displayName,
            onValueChange = editAccountInteractions::onDisplayNameTextChanged,
            label = {
                Text(text = stringResource(id = R.string.edit_account_display_name_label))
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        MoSoTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = editAccountUiState.bio,
            onValueChange = editAccountInteractions::onBioTextChanged,
            label = {
                Text(text = stringResource(id = R.string.edit_account_bio_label))
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "${editAccountUiState.bioCharacterCount}/500")
    }
}