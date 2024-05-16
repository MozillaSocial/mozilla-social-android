package social.firefly.feature.settings.contentpreferences.blockedDomains

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import social.firefly.core.repository.mastodon.DomainBlocksRepository
import social.firefly.core.repository.paging.sources.DomainBlocksPagingSource
import social.firefly.core.usecase.mastodon.account.BlockDomain
import social.firefly.core.usecase.mastodon.account.UnblockDomain
import timber.log.Timber

class BlockedDomainsViewModel(
    private val domainBlocksRepository: DomainBlocksRepository,
    private val blockDomain: BlockDomain,
    private val unblockDomain: UnblockDomain,
) : ViewModel(), BlockedDomainInteractions {

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
            )
        }
    }.cachedIn(viewModelScope)

    override fun onUnblockClicked(domain: String) {
        viewModelScope.launch {
            try {
                unblockDomain(domain)
            } catch (e: UnblockDomain.UnblockDomainFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onBlockClicked(domain: String) {
        viewModelScope.launch {
            try {
                blockDomain(domain)
            } catch (e: BlockDomain.BlockDomainFailedException) {
                Timber.e(e)
            }
        }
    }
}