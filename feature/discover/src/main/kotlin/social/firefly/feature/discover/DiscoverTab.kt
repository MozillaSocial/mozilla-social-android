package social.firefly.feature.discover

import social.firefly.common.utils.StringFactory

sealed class DiscoverTab(
    val tabTitle: StringFactory,
) {
    data object Hashtags : DiscoverTab(
        tabTitle = StringFactory.resource(R.string.hashtags_tab),
    )

    data object Posts : DiscoverTab(
        tabTitle = StringFactory.resource(R.string.posts_tab),
    )
}