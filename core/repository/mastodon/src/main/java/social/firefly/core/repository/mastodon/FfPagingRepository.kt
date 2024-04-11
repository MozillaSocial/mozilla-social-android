package social.firefly.core.repository.mastodon

interface FFRemoteSource<T> {
    suspend fun getRemotely(limit: Int, offset: Int): List<T>
}

interface FFLocalSource<T> {
    suspend fun saveLocally(currentPage: List<PageItem<T>>)
}

data class PageItem<T>(val position: Int, val item: T)
