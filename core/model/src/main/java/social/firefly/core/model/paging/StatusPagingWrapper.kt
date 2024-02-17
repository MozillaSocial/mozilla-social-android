package social.firefly.core.model.paging

import social.firefly.common.MastodonPagingLink
import social.firefly.core.model.Status

data class StatusPagingWrapper(
    val statuses: List<Status>,
    val pagingLinks: List<MastodonPagingLink>?,
)
