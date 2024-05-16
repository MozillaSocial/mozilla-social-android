package social.firefly.feature.settings.contentpreferences.blockedDomains

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.button.FfButton
import social.firefly.core.ui.common.button.FfButtonPrimaryDefaults
import social.firefly.core.ui.common.button.FfButtonSecondaryDefaults
import social.firefly.core.ui.common.pullrefresh.PullRefreshLazyColumn
import social.firefly.core.ui.common.text.LargeTextBody
import social.firefly.feature.settings.R
import social.firefly.feature.settings.ui.SettingsColumn

@Composable
internal fun BlockedDomainsScreen (
    viewModel: BlockedDomainsViewModel = koinViewModel()
) {
    BlockedDomainsScreen(
        feed = viewModel.blockedDomains,
        blockedDomainInteractions = viewModel,
    )
}

@Composable
private fun BlockedDomainsScreen(
    feed: Flow<PagingData<BlockedDomainState>>,
    blockedDomainInteractions: BlockedDomainInteractions,
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
                    key = lazyPagingItems.itemKey { it.domain },
                ) { index ->
                    lazyPagingItems[index]?.let { item ->
                        Domain(
                            uiState = item,
                            blockedDomainInteractions = blockedDomainInteractions,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Domain(
    uiState: BlockedDomainState,
    blockedDomainInteractions: BlockedDomainInteractions,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LargeTextBody(
            modifier = Modifier.weight(1f),
            text = uiState.domain,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        var isBlocked by remember {
            mutableStateOf(true)
        }

        FfButton(
            theme = if (isBlocked) {
                FfButtonSecondaryDefaults
            } else {
                FfButtonPrimaryDefaults
            },
            onClick = {
                if (isBlocked) {
                    blockedDomainInteractions.onUnblockClicked(uiState.domain)
                } else {
                    blockedDomainInteractions.onBlockClicked(uiState.domain)
                }
                isBlocked = !isBlocked
            }
        ) {
            Text(
                text = stringResource(
                    id = if (isBlocked) {
                        R.string.unblock
                    } else {
                        R.string.block
                    }
                )
            )
        }
    }
}
