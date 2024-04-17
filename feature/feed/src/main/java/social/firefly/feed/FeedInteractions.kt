package social.firefly.feed

interface FeedInteractions {
    fun onTabClicked(timelineType: TimelineType) = Unit
    fun onScreenViewed() = Unit
    fun onStatusViewed(statusId: String) = Unit
    suspend fun onScrollToTopClicked(onDatabaseCleared: suspend () -> Unit) = Unit
    fun topOfFeedReached() = Unit
}
