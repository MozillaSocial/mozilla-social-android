package social.firefly.core.model.paging

import social.firefly.common.HeaderLink
import social.firefly.core.model.Account

data class AccountPagingWrapper(
    val accounts: List<Account>,
    val nextPage: HeaderLink?,
    val prevPage: HeaderLink?,
)
