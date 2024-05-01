package social.firefly.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import social.firefly.core.analytics.FeedLocation
import social.firefly.core.repository.mastodon.FavoritesRepository
import social.firefly.core.repository.paging.remotemediators.FavoritesRemoteMediator
import social.firefly.core.ui.postcard.PostCardDelegate
import social.firefly.core.ui.postcard.toPostCardUiState
import social.firefly.core.usecase.mastodon.account.GetLoggedInUserAccountId

class FavoritesViewModel(
    favoritesRepository: FavoritesRepository,
    favoritesRemoteMediator: FavoritesRemoteMediator,
    userAccountId: GetLoggedInUserAccountId,
) : ViewModel(), KoinComponent {

    val postCardDelegate by inject<PostCardDelegate> {
        parametersOf(FeedLocation.FAVORITES)
    }

    @OptIn(ExperimentalPagingApi::class)
    val feed = favoritesRepository.getFavoritesPager(
        remoteMediator = favoritesRemoteMediator,
    ).map { pagingData ->
        pagingData.map {
            it.toPostCardUiState(userAccountId())
        }
    }.cachedIn(viewModelScope)
}