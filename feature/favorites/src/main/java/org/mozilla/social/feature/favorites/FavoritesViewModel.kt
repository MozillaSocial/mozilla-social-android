package org.mozilla.social.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.repository.mastodon.FavoritesRepository
import org.mozilla.social.core.repository.paging.FavoritesRemoteMediator
import org.mozilla.social.core.analytics.FeedLocation
import org.mozilla.social.core.ui.postcard.PostCardDelegate
import org.mozilla.social.core.ui.postcard.toPostCardUiState
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId

class FavoritesViewModel(
    favoritesRepository: FavoritesRepository,
    favoritesRemoteMediator: FavoritesRemoteMediator,
    userAccountId: GetLoggedInUserAccountId,
) : ViewModel(), KoinComponent {

    val postCardDelegate by inject<PostCardDelegate> {
        parametersOf(viewModelScope, FeedLocation.FAVORITES)
    }

    @OptIn(ExperimentalPagingApi::class)
    val feed = favoritesRepository.getFavoritesPager(
        remoteMediator = favoritesRemoteMediator,
    ).map { pagingData ->
        pagingData.map {
            it.toPostCardUiState(userAccountId(), postCardDelegate)
        }
    }.cachedIn(viewModelScope)
}