package org.mozilla.social.core.ui.common.account.quickview

import org.mozilla.social.core.model.Account

fun Account.toQuickViewUiState(): AccountQuickViewUiState = AccountQuickViewUiState(
    accountId = accountId,
    displayName = displayName,
    webFinger = acct,
    avatarUrl = avatarUrl,
)