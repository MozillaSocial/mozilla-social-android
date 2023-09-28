package org.mozilla.social.core.ui.account.quickview

import org.mozilla.social.model.Account

fun Account.toQuickViewUiState(): AccountQuickViewUiState = AccountQuickViewUiState(
    accountId = accountId,
    displayName = displayName,
    webFinger = acct,
    avatarUrl = avatarUrl,
)