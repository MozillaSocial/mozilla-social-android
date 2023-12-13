package org.mozilla.social.feature.settings.contentpreferences.blockedusers

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.account.quickview.AccountQuickView
import org.mozilla.social.core.ui.common.account.quickview.AccountQuickViewUiState
import org.mozilla.social.core.ui.common.button.MoSoButton
import org.mozilla.social.core.ui.common.button.MoSoButtonPrimaryDefaults
import org.mozilla.social.core.ui.common.button.MoSoButtonSecondary
import org.mozilla.social.core.ui.common.button.MoSoButtonSecondaryDefaults
import org.mozilla.social.core.ui.common.error.GenericError
import org.mozilla.social.core.ui.common.text.SmallTextLabel
import org.mozilla.social.feature.settings.R
import org.mozilla.social.feature.settings.ui.SettingsColumn

@Composable
fun BlockedUsersSettingsScreen(viewModel: BlockedUsersViewModel = koinViewModel()) {
    BlockedUsersSettingsScreen(viewModel.blocks, viewModel::onBlockButtonClicked)

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@Composable
fun BlockedUsersSettingsScreen(
    blocksPagingData: Flow<PagingData<BlockedUserState>>,
    onUnblockClicked: (accountId: String) -> Unit,
) {
    MoSoSurface {
        SettingsColumn(title = stringResource(id = R.string.blocked_users_title)) {
            BlockedUserList(
                blocksPagingData = blocksPagingData,
                onUnblockClicked = onUnblockClicked
            )
        }
    }
}

@Composable
fun BlockedUserList(
    blocksPagingData: Flow<PagingData<BlockedUserState>>,
    onUnblockClicked: (accountId: String) -> Unit
) {
    val lazyPagingItems: LazyPagingItems<BlockedUserState> =
        blocksPagingData.collectAsLazyPagingItems()

    LazyColumn {
        when (lazyPagingItems.loadState.refresh) {
            is LoadState.Error -> {}
            else ->
                items(
                    count = lazyPagingItems.itemCount,
                    key = lazyPagingItems.itemKey { it.account.accountId },
                ) { index ->
                    lazyPagingItems[index]?.let { block ->
                        BlockedUserRow(
                            block = block.account,
                            isBlocked = block.isBlocked,
                            onUnblockClicked = onUnblockClicked
                        )
                    }
                }
        }

        when (lazyPagingItems.loadState.append) {
            is LoadState.Loading -> {
                item {
                    CircularProgressIndicator(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .padding(16.dp),
                    )
                }
            }

            is LoadState.Error -> {
                item {
                    GenericError(
                        onRetryClicked = { lazyPagingItems.retry() },
                    )
                }
            }

            is LoadState.NotLoading -> {}
        }
    }
}

@Composable
fun BlockedUserRow(
    block: AccountQuickViewUiState,
    isBlocked: Boolean,
    onUnblockClicked: (accountId: String) -> Unit,
) {
    var openAlertDialog by remember { mutableStateOf(false) }
    if (openAlertDialog) {
        ConfirmationDialog(acct = block.webFinger,
            onDismissRequest = {
                openAlertDialog = false
            },
            onConfirmed = {
                openAlertDialog = false
                onUnblockClicked(block.accountId)
            })
    }
    Row(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        AccountQuickView(
            uiState = block,
            modifier = Modifier.fillMaxWidth(),
            buttonSlot = {
                MoSoButton(
                    onClick = {
                        openAlertDialog = true
                    },
                    colors = if (isBlocked) MoSoButtonPrimaryDefaults.colors() else MoSoButtonSecondaryDefaults.colors()
                ) {
                    SmallTextLabel(text = stringResource(id = R.string.unblock))
                }
            },
        )
    }
}

@Composable
fun ConfirmationDialog(acct: String, onDismissRequest: () -> Unit, onConfirmed: () -> Unit) {
    AlertDialog(
        title = {
            Text(
                text = stringResource(
                    id = R.string.are_you_sure_you_want_to_unblock,
                    acct,
                )
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            MoSoButton(
                onClick = onConfirmed,
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            MoSoButtonSecondary(
                onClick = onDismissRequest
            ) {
                Text("Cancel")
            }
        }
    )
}

data class BlockedUserState(val isBlocked: Boolean, val account: AccountQuickViewUiState)