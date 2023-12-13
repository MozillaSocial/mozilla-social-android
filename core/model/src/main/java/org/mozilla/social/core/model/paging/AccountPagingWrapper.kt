package org.mozilla.social.core.model.paging

import org.mozilla.social.common.HeaderLink
import org.mozilla.social.common.MastodonPagingLink
import org.mozilla.social.core.model.Account

data class AccountPagingWrapper(
    val accounts: List<Account>,
    val nextPage: HeaderLink?,
    val prevPage: HeaderLink?,
)
