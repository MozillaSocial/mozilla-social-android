package org.mozilla.social.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.common.Resource
import org.mozilla.social.core.analytics.DiscoverAnalytics
import org.mozilla.social.core.analytics.utils.ImpressionTracker
import org.mozilla.social.core.model.Recommendation
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.usecase.mozilla.GetRecommendations
import timber.log.Timber

class DiscoverViewModel(
    private val getRecommendations: GetRecommendations,
    private val analytics: DiscoverAnalytics,
    private val navigateTo: NavigateTo,
) : ViewModel(), DiscoverInteractions {
    private val _recommendations =
        MutableStateFlow<Resource<List<Recommendation>>>(Resource.Loading())
    val recommendations = _recommendations.asStateFlow()

    private val recommendationImpressionTracker =
        ImpressionTracker<String> { recommendationId ->
            analytics.recommendationViewed(recommendationId)
        }

    init {
        getRecs()
    }

    private fun getRecs() {
        _recommendations.update { Resource.Loading() }
        viewModelScope.launch {
            try {
                _recommendations.update {
                    Resource.Loaded(getRecommendations())
                }
            } catch (e: Exception) {
                Timber.e(e)
                _recommendations.update { Resource.Error(e) }
            }
        }
    }

    override fun onRetryClicked() {
        getRecs()
    }

    override fun onRecommendationClicked(recommendationId: String) {
        analytics.recommendationOpened(recommendationId)
    }

    override fun onShareClicked(recommendationId: String) {
        analytics.recommendationShared(recommendationId)
    }

    override fun onScreenViewed() {
        analytics.discoverScreenViewed()
    }

    override fun onRecommendationViewed(recommendationId: String) {
        recommendationImpressionTracker.track(recommendationId)
    }

    override fun onSearchBarClicked() {
        navigateTo(NavigationDestination.Search)
    }
}
