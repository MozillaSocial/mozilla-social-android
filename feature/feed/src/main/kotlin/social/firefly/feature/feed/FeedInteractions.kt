package social.firefly.feature.feed

interface FeedInteractions {
    fun onTabClicked(timelineType: TimelineType) = Unit
    fun onScreenViewed() = Unit
    suspend fun onScrollToTopClicked(onDatabaseCleared: suspend () -> Unit) = Unit
    fun onFirstVisibleItemIndexForHomeChanged(
        index: Int,
        statusId: String,
    ) = Unit
    fun onHomePrependEndReached(reached: Boolean) = Unit
    fun onGoToDiscoverClicked() = Unit
}
