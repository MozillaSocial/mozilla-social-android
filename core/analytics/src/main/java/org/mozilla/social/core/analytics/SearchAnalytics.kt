package org.mozilla.social.core.analytics

import org.mozilla.social.core.analytics.core.Analytics
import org.mozilla.social.core.analytics.core.EngagementType

class SearchAnalytics internal constructor(private val analytics: Analytics) {

    fun searchClicked(query: String) {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = SEARCH_QUERY,
            uiAdditionalDetail = query,
        )
    }

    fun searchTabClicked(tab: SearchTab) {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = tab.identifier,
        )
    }

    fun followClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = SEARCH_ACCOUNT_FOLLOW,
        )
    }

    fun accountClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = SEARCH_ACCOUNT_CLICKED,
        )
    }

    fun hashtagClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = SEARCH_HASHTAG_CLICKED,
        )
    }

    fun hashtagFollowClicked() {
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = SEARCH_HASHTAG_FOLLOW,
        )
    }

    companion object {
        private const val SEARCH_QUERY = "search.query"
        private const val SEARCH_TAB_ACCOUNTS = "search.tab.accounts"
        private const val SEARCH_TAB_POSTS = "search.tab.posts"
        private const val SEARCH_TAB_HASHTAGS = "search.tab.hashtags"
        private const val SEARCH_TAB_TOP = "search.tab.top"
        private const val SEARCH_ACCOUNT_CLICKED = "search.account.clicked"
        private const val SEARCH_HASHTAG_CLICKED = "search.hashtag.clicked"
        private const val SEARCH_ACCOUNT_FOLLOW = "search.account.follow"
        private const val SEARCH_HASHTAG_FOLLOW = "search.hashtag.follow"
    }

    enum class SearchTab(val identifier: String) {
        TOP(SEARCH_TAB_TOP), ACCOUNTS(SEARCH_TAB_ACCOUNTS), HASHTAGS(SEARCH_TAB_HASHTAGS), POSTS(SEARCH_TAB_POSTS)
    }
}
