package social.firefly.core.repository.common

interface FFRemoteSource<T> {
    suspend fun getRemotely(limit: Int, offset: Int): List<T>
}