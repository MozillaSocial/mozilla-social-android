package org.mozilla.social.search

import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.EngagementType

class SearchAnalytics(private val analytics: Analytics) {

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
            uiIdentifier = tab.toIdentifier(),
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
        const val SEARCH_QUERY = "search.query"
        const val SEARCH_TAB_ACCOUNTS = "search.tab.accounts"
        const val SEARCH_TAB_POSTS = "search.tab.posts"
        const val SEARCH_TAB_HASHTAGS = "search.tab.hashtags"
        const val SEARCH_TAB_TOP = "search.tab.top"
        const val SEARCH_ACCOUNT_CLICKED = "search.account.clicked"
        const val SEARCH_HASHTAG_CLICKED = "search.hashtag.clicked"
        const val SEARCH_ACCOUNT_FOLLOW = "search.account.follow"
        const val SEARCH_HASHTAG_FOLLOW = "search.hashtag.follow"
    }
}

private fun SearchTab.toIdentifier(): String {
    return when (this) {
        SearchTab.POSTS -> SearchAnalytics.SEARCH_TAB_POSTS
        SearchTab.ACCOUNTS -> SearchAnalytics.SEARCH_TAB_ACCOUNTS
        SearchTab.HASHTAGS -> SearchAnalytics.SEARCH_TAB_HASHTAGS
        SearchTab.TOP -> SearchAnalytics.SEARCH_TAB_TOP
    }
}