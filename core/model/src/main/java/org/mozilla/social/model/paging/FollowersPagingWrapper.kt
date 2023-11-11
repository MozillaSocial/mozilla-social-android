package org.mozilla.social.model.paging

import org.mozilla.social.model.Account

data class FollowersPagingWrapper(
    val accounts: List<Account>,
    val link: String?,
)