package org.mozilla.social.feed

interface FeedInteractions {
    fun onTabClicked(timelineType: TimelineType) = Unit

    fun onScreenViewed() = Unit
}