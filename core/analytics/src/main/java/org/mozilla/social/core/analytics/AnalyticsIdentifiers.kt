package org.mozilla.social.core.analytics

object AnalyticsIdentifiers {
    const val DISCOVER_RECOMMENDATION_IMPRESSION = "discover.recommendation.impression"
    const val DISCOVER_SCREEN_IMPRESSION = "discover.screen.impression"
    const val DISCOVER_RECOMMENDATION_OPEN = "discover.recommendation.open"
    const val DISCOVER_RECOMMENDATION_SHARE = "discover.recommendation.share"
    const val DISCOVER_RECOMMENDATION_BOOKMARK = "discover.recommendation.bookmark"
    const val DISCOVER_RECOMMENDATION_REPOST = "discover.recommendation.repost"

    const val ACCOUNTS_SCREEN_IMPRESSION = "account.screen.impression"
    const val ACCOUNTS_SCREEN_FOLLOW = "profile.follow_btn.follow"
    const val ACCOUNTS_SCREEN_UNFOLLOW = "profile.follow_btn.unfollow"
    const val PROFILE_EDIT_PROFILE = "profile.edit.profile"
    const val PROFILE_EDIT_PROFILE_SCREEN_IMPRESSION = "profile.edit.profile.screen.impression"
    const val PROFILE_EDIT_PROFILE_SAVE = "profile.edit.profile.save"

    const val SETTINGS_SCREEN_IMPRESSION = "settings.screen.impression"
    const val SETTINGS_ACCOUNT_IMPRESSION = "settings.account.impression"
    const val SETTINGS_CONTENT_PREFERENCES_IMPRESSION = "settings.content-preferences.impression"
    const val SETTINGS_CONTENT_OPEN_SOURCE_LICENSE = "settings.open-source-licenses.impression"
    const val MUTED_USERS_SCREEN_IMPRESSION = "muted.users.screen.impression"
    const val BLOCKED_USERS_SCREEN_IMPRESSION = "blocked.users.screen.impression"
    const val SETTINGS_PRIVACY_IMPRESSION = "settings.privacy.impression"
    const val SETTINGS_ABOUT_IMPRESSION = "settings.about.impression"
    const val PRIVACY_COLLECT_DATA_TOGGLE = "privacy.collect-data.toggle"
    const val SETTINGS_ACCOUNT_SIGNOUT = "account.signout"

    const val NEW_POST_SCREEN_IMPRESSION = "new.post.screen.impression"

    const val FEED_SCREEN_IMPRESSION = "feed.screen.impression"
    const val FEED_POST_VOTE = "feed.post.vote"
    const val FEED_POST_REPLY = "feed.post.open-reply"
    const val FEED_POST_BOOST = "feed.post.reblog"
    const val FEED_POST_UNBOOST = "feed.post.unreblog"
    const val FEED_POST_FAVORITE = "feed.post.favourite"
    const val FEED_POST_UNFAVORITE = "feed.post.unfavourite"
    const val FEED_POST_LINK_TAPPED = "post.link.tap"
    const val FEED_POST_ACCOUNT_TAPPED = "post.account.tap"
    const val FEED_POST_ACCOUNT_IMAGE_TAPPED = "post.account.image.tap"
    const val FEED_POST_HASHTAG_TAPPED = "post.hashtag.tap"
    const val FEED_POST_MUTE = "feed.post.mute"
    const val FEED_POST_BLOCK = "feed.post.block"
    const val FEED_POST_REPORT = "feed.post.block"

    const val FEED_PREFIX_PROFILE = "profile"
    const val FEED_PREFIX_HOME = "home"
    const val FEED_PREFIX_LOCAL = "local"
    const val FEED_PREFIX_FEDERATED = "federated"
    const val FEED_PREFIX_HASHTAG = "hashtag"
    const val FEED_PREFIX_THREAD = "thread"
    const val FEED_PREFIX_FAVORITES = "favorites"
    const val FEED_PREFIX_SEARCH = "search"
    const val FEED_PREFIX_NOTIFICATIONS = "notifications"

    const val FEED_HOME_SCREEN_HOME = "feed.screen.home"
    const val FEED_LOCAL_SCREEN_HOME = "feed.screen.local"
    const val FEED_FEDERATED_SCREEN_HOME = "feed.screen.federated"

    const val REPORT_SCREEN_IMPRESSION = "report.screen.impression"

    const val THREAD_SCREEN_IMPRESSION = "thread.screen.impression"

    const val FOLLOWERS_SCREEN_IMPRESSION = "followers.screen.impression"

    const val HASHTAG_SCREEN_IMPRESSION = "hashtag.screen.impression"

    const val AUTH_SCREEN_IMPRESSION = "auth.screen.impression"
    const val AUTH_SCREEN_SIGN_IN_SIGN_UP = "auth.screen.sign-in-sign-up"
    const val AUTH_SCREEN_CHOOSE_A_SERVER = "auth.screen.choose-a-server"

    const val CHOOSE_A_SERVER_SCREEN_IMPRESSION = "choose.a.server.screen.impression"
    const val CHOOSE_A_SERVER_SCREEN_SUBMIT_SERVER = "choose.a.server.screen.submit-server"

    const val PROFILE_OVERFLOW_SHARE = "profile.more.share-account"
    const val PROFILE_OVERFLOW_MUTE = "profile.more.mute"
    const val PROFILE_OVERFLOW_UNMUTE = "profile.more.unmute"
    const val PROFILE_OVERFLOW_BLOCK = "profile.more.block"
    const val PROFILE_OVERFLOW_UNBLOCK = "profile.more.unblock"
    const val PROFILE_OVERFLOW_REPORT = "profile.more.report.open"
    const val PROFILE_FEED_POSTS = "profile.tabs.posts"
    const val PROFILE_FEED_POSTS_AND_REPLIES = "profile.tabs.posts-and-replies"
    const val PROFILE_FEED_MEDIA = "profile.tabs.media"

    const val NEW_POST_POST = "new.post.post"
    const val NEW_POST_IMAGE = "new.post.image"
    const val NEW_POST_MEDIA = "new.post.media"
    const val NEW_POST_POLL = "new.post.poll"
    const val NEW_POST_CONTENT_WARNING = "new.post.content.warning"

    const val FOLLOWS_SCREEN_FOLLOW = "follows.screen.follow"
    const val FOLLOWS_SCREEN_UNFOLLOW = "follows.screen.unfollow"

    const val SEARCH_QUERY = "search.query"
    const val SEARCH_TAB_ACCOUNTS = "search.tab.accounts"
    const val SEARCH_TAB_POSTS = "search.tab.posts"
    const val SEARCH_TAB_HASHTAGS = "search.tab.hashtags"
    const val SEARCH_TAB_TOP = "search.tab.top"
    const val SEARCH_ACCOUNT_CLICKED = "search.account.clicked"
    const val SEARCH_HASHTAG_CLICKED = "search.hashtag.clicked"
    const val SEARCH_ACCOUNT_FOLLOW = "search.account.follow"
    const val SEARCH_HASHTAG_FOLLOW = "search.hashtag.follow"

    const val HASHTAG_FOLLOW = "hashtag.follow"
}
