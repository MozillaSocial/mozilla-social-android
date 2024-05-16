package social.firefly.feature.settings.contentpreferences.blockedDomains

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.pullrefresh.PullRefreshLazyColumn
import social.firefly.feature.settings.R
import social.firefly.feature.settings.ui.SettingsColumn

@Composable
internal fun BlockedDomainsScreen (
    viewModel: BlockedDomainsViewModel = koinViewModel()
) {
    BlockedDomainsScreen(
        feed = viewModel.blockedDomains
    )
}

@Composable
private fun BlockedDomainsScreen(
    feed: Flow<PagingData<BlockedDomainState>>
) {
    FfSurface {
        SettingsColumn(
            modifier = Modifier
                .padding(horizontal = FfSpacing.md),
            title = stringResource(id = R.string.blocked_domains_title)
        ) {
            val lazyPagingItems = feed.collectAsLazyPagingItems()
            PullRefreshLazyColumn(
                lazyPagingItems = lazyPagingItems
            ) {
                items(
                    count = lazyPagingItems.itemCount,
                    key = lazyPagingItems.itemKey { it },
                ) { index ->
                    lazyPagingItems[index]?.let { item ->
                        Text(text = item.domain)
                    }
                }
            }
        }
    }
}