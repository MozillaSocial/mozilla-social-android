package org.mozilla.social.feature.followers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
        uiState = viewModel.followersUiState.collectAsState().value,
        followersInteractions = viewModel,
    )
}

@Composable
private fun FollowersScreen(
    uiState: Resource<List<AccountQuickViewUiState>>,
    followersInteractions: FollowersInteractions,
) {
    Column {
        MoSoTopBar(
            title = stringResource(id = R.string.followers),
            onIconClicked = { followersInteractions.onCloseClicked() }
        )
        when (uiState) {
            is Resource.Loading -> {
                //TODO loading state
            }

            is Resource.Loaded -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    uiState.data.forEach {
                        AccountQuickView(
                            uiState = it,
                            onClick = followersInteractions::onAccountClicked
                        )
                        MoSoDivider()
                    }
                }
            }

            is Resource.Error -> {
                //TODO error state
            }
        }
    }
}