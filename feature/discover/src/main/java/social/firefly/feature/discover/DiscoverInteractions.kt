package social.firefly.feature.discover

interface DiscoverInteractions {
    fun onScreenViewed()
    fun onSearchBarClicked()
    fun onTabClicked(tab: DiscoverTab)
}

object DiscoverInteractionsNoOp : DiscoverInteractions {
    override fun onScreenViewed() = Unit
    override fun onSearchBarClicked() = Unit
    override fun onTabClicked(tab: DiscoverTab) = Unit
}
