package social.firefly.core.ui.common.account.quickview

import social.firefly.core.model.Account

fun Account.toQuickViewUiState(): AccountQuickViewUiState =
    AccountQuickViewUiState(
        accountId = accountId,
        displayName = displayName,
        webFinger = acct,
        avatarUrl = avatarUrl,
    )
