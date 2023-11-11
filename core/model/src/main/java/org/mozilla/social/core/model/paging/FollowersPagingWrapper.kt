package org.mozilla.social.core.model.paging

import org.mozilla.social.core.model.Account

data class FollowersPagingWrapper(
    val accounts: List<Account>,
    val link: String?,
)