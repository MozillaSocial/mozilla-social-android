package social.firefly.feature.settings.contentpreferences.blockedDomains

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.map
import social.firefly.core.repository.mastodon.DomainBlocksRepository
import social.firefly.core.repository.paging.sources.DomainBlocksPagingSource

class BlockedDomainsViewModel(
    private val domainBlocksRepository: DomainBlocksRepository,
) : ViewModel() {

    private val toggledDomains: List<String> = emptyList()

    val blockedDomains = Pager(
        config = PagingConfig(
            pageSize = 40,
            initialLoadSize = 40,
        )
    ) {
        DomainBlocksPagingSource(domainBlocksRepository)
    }.flow.map {
        it.map { domain ->
            BlockedDomainState(
                domain = domain,
                isBlocked = !toggledDomains.contains(domain)
            )
        }
    }.cachedIn(viewModelScope)
}