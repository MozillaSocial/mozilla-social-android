package org.mozilla.social.core.data.repository.model.followers

import org.mozilla.social.model.Account

data class FollowersPagingWrapper(
    val accounts: List<Account>,
    val link: String?,
)