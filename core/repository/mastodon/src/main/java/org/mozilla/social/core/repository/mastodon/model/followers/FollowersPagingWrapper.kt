package org.mozilla.social.core.repository.mastodon.model.followers

import org.mozilla.social.model.Account

data class FollowersPagingWrapper(
    val accounts: List<Account>,
    val link: String?,
)