package social.firefly.feed

interface FeedInteractions {
    fun onTabClicked(timelineType: TimelineType) = Unit
    fun onScreenViewed() = Unit
}
