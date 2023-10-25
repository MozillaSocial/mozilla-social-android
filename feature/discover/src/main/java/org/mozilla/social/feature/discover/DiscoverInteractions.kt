package org.mozilla.social.feature.discover

interface DiscoverInteractions {
    fun onRetryClicked() = Unit
    fun onRepostClicked() = Unit
    fun onBookmarkClicked() = Unit
    fun onShareClicked() = Unit
}