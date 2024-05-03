package social.firefly.feature.followedHashTags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.map
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import social.firefly.core.repository.paging.pagers.FollowedHashTagsPager
import social.firefly.core.ui.hashtagcard.HashTagCardDelegate
import social.firefly.core.ui.hashtagcard.quickview.toHashTagQuickViewUiState

class FollowedHashTagsViewModel(
    followedHashTagsPager: FollowedHashTagsPager,
) : ViewModel(), KoinComponent {

    val hashTagCardDelegate: HashTagCardDelegate by inject {
        parametersOf(viewModelScope)
    }

    @OptIn(ExperimentalPagingApi::class)
    val feed = followedHashTagsPager.build()
        .map { pagingData ->
            pagingData.map { hashtag ->
                hashtag.toHashTagQuickViewUiState()
            }
        }
}