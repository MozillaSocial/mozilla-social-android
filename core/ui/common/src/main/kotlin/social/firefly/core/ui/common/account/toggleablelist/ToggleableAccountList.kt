package social.firefly.core.ui.common.account.toggleablelist

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import social.firefly.common.utils.StringFactory
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.ui.common.account.quickview.AccountQuickView
import social.firefly.core.ui.common.account.quickview.AccountQuickViewUiState
import social.firefly.core.ui.common.button.FfButton
import social.firefly.core.ui.common.button.FfButtonContentPadding
import social.firefly.core.ui.common.button.FfButtonSecondary
import social.firefly.core.ui.common.button.FfButtonTheme
import social.firefly.core.ui.common.error.GenericError
import social.firefly.core.ui.common.pullrefresh.PullRefreshLazyColumn
import social.firefly.core.ui.common.text.SmallTextLabel

@Composable
fun <B : ToggleableButtonState, A : ToggleableAccountListItemState<B>> ToggleableAccountList(
    pagingData: Flow<PagingData<A>>,
    onAccountClicked: (accountId: String) -> Unit,
    onButtonClicked: (accountId: String, buttonState: B) -> Unit,
) {
    val lazyPagingItems: LazyPagingItems<A> = pagingData.collectAsLazyPagingItems()

    PullRefreshLazyColumn(lazyPagingItems = lazyPagingItems) {
        when (lazyPagingItems.loadState.refresh) {
            is LoadState.Error -> {}
            else ->
                items(
                    count = lazyPagingItems.itemCount,
                    key = lazyPagingItems.itemKey { it.account.accountId },
                ) { index ->
                    lazyPagingItems[index]?.let { account ->
                        UserRow(
                            account = account.account,
                            buttonState = account.buttonState,
                            onAccountClicked = onAccountClicked,
                            onButtonClicked = onButtonClicked
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
private fun <B : ToggleableButtonState> UserRow(
    account: AccountQuickViewUiState,
    buttonState: B,
    onAccountClicked: (accountId: String) -> Unit,
    onButtonClicked: (accountId: String, buttonState: B) -> Unit
) {
    var openConfirmationDialog: String? by remember { mutableStateOf(null) }

    openConfirmationDialog?.let { dialogTitleText ->
        ConfirmationDialog(title = dialogTitleText,
            onDismissRequest = {
                openConfirmationDialog = null
            },
            onConfirmed = {
                openConfirmationDialog = null
                onButtonClicked(account.accountId, buttonState)
            })

    }

    Row(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        val context = LocalContext.current
        AccountQuickView(
            uiState = account,
            modifier = Modifier
                .clickable { onAccountClicked(account.accountId) }
                .fillMaxWidth()
                .padding(vertical = FfSpacing.md),
            buttonSlot = {
                FfButton(
                    onClick = {
                        buttonState.confirmationText?.let {
                            openConfirmationDialog = it.build(
                                context
                            )
                        } ?: onButtonClicked(account.accountId, buttonState)
                    },
                    theme = buttonState.theme,
                    contentPadding = FfButtonContentPadding.small,
                ) {
                    SmallTextLabel(text = stringResource(id = buttonState.text))
                }
            },
        )
    }
}

@Composable
private fun ConfirmationDialog(
    title: String,
    onDismissRequest: () -> Unit,
    onConfirmed: () -> Unit
) {
    AlertDialog(
        title = {
            Text(
                text = title,
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            FfButton(
                onClick = onConfirmed,
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            FfButtonSecondary(
                onClick = onDismissRequest
            ) {
                Text("Cancel")
            }
        }
    )
}

data class ToggleableAccountListItemState<T : ToggleableButtonState>(
    val buttonState: T,
    val account: AccountQuickViewUiState
)

/**
 * @property text
 * @property confirmationText to show when confirming the action. If this is null, a confirmation
 * dialog will not be shown
 * @property theme for the button
 */
interface ToggleableButtonState {
    @get:StringRes
    val text: Int
    val confirmationText: StringFactory?
    val theme: FfButtonTheme
}