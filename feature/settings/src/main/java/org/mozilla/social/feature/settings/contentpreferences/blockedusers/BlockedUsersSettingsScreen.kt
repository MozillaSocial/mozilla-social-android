package org.mozilla.social.feature.settings.contentpreferences.blockedusers

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.account.quickview.AccountQuickView
import org.mozilla.social.core.ui.common.account.quickview.AccountQuickViewUiState
import org.mozilla.social.core.ui.common.button.MoSoButtonSecondary
import org.mozilla.social.core.ui.common.text.SmallTextLabel
import org.mozilla.social.feature.settings.R
import org.mozilla.social.feature.settings.ui.SettingsColumn

@Composable
fun BlockedUsersSettingsScreen(viewModel: BlockedUsersViewModel = koinViewModel()) {
    val blocks by viewModel.blocks.collectAsStateWithLifecycle(initialValue = listOf())
    BlockedUsersSettingsScreen(blocks, viewModel::onBlockButtonClicked)

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@Composable
fun BlockedUsersSettingsScreen(
    blocks: List<Block>,
    onClick: (accountId: String) -> Unit,
) {
    MoSoSurface {
        SettingsColumn(title = stringResource(id = R.string.blocked_users_title)) {
            for (block in blocks) {
                BlockedUserRow(block = block, onClick = onClick)
            }
        }
    }
}

@Composable
fun BlockedUserRow(
    block: Block,
    onClick: (accountId: String) -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        AccountQuickView(
            uiState = block.quickViewUiState,
            modifier = Modifier.fillMaxWidth(),
            buttonSlot = {
                MoSoButtonSecondary(
                    enabled = false,
                    onClick = { onClick(block.quickViewUiState.accountId) },
                ) { SmallTextLabel(text = stringResource(id = R.string.unblock)) }
            },
        )
    }
}

data class Block(
    val quickViewUiState: AccountQuickViewUiState,
)
