package org.mozilla.social.feature.followers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.common.Resource
import org.mozilla.social.core.designsystem.component.MoSoDivider
import org.mozilla.social.core.designsystem.component.MoSoTopBar
import org.mozilla.social.core.ui.account.quickview.AccountQuickView
import org.mozilla.social.core.ui.account.quickview.AccountQuickViewUiState

@Composable
internal fun FollowersRoute(
    accountId: String,
    followersNavigationCallbacks: FollowersNavigationCallbacks,
    viewModel: FollowersViewModel = koinViewModel(
        parameters = { parametersOf(
            accountId,
            followersNavigationCallbacks,
        ) }
    )
) {
    FollowersScreen(
        followers = viewModel.followers,
        followersInteractions = viewModel,
    )
}

@Composable
private fun FollowersScreen(
    followers: Flow<PagingData<AccountQuickViewUiState>>,
    followersInteractions: FollowersInteractions,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MoSoTopBar(
            title = stringResource(id = R.string.followers),
            onIconClicked = { followersInteractions.onCloseClicked() }
        )

        val lazyPagingItems = followers.collectAsLazyPagingItems()

        //TODO loading and error states and pull to refresh
        LazyColumn {
            items(count = lazyPagingItems.itemCount) { index ->
                lazyPagingItems[index]?.let { uiState ->
                    AccountQuickView(
                        uiState = uiState,
                        onClick = followersInteractions::onAccountClicked
                    )
                    MoSoDivider()
                }

            }
        }
    }
}