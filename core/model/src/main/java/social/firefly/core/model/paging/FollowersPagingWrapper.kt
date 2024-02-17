package social.firefly.core.model.paging

import social.firefly.common.MastodonPagingLink
import social.firefly.core.model.Account

data class FollowersPagingWrapper(
    val accounts: List<Account>,
    val pagingLinks: List<MastodonPagingLink>?,
)
