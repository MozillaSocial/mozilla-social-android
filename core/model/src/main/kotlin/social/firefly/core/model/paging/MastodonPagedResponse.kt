package social.firefly.core.model.paging

import social.firefly.common.MastodonPagingLink

data class MastodonPagedResponse<T>(
    val items: List<T>,
    val pagingLinks: List<MastodonPagingLink>?,
)