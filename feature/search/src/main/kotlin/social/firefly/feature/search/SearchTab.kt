package social.firefly.feature.search

import social.firefly.common.utils.StringFactory
import social.firefly.feature.search.R

enum class SearchTab(val tabTitle: StringFactory) {
    TOP(StringFactory.resource(R.string.top_tab)),
    ACCOUNTS(StringFactory.resource(R.string.accounts_tab)),
    POSTS(StringFactory.resource(R.string.posts_tab)),
    HASHTAGS(StringFactory.resource(R.string.hashtags_tab)),
}