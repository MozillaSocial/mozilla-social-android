package social.firefly.core.repository.mastodon

import androidx.paging.PagingSource
import social.firefly.core.database.dao.MutesDao
import social.firefly.core.database.model.entities.accountCollections.DatabaseMute
import social.firefly.core.database.model.entities.accountCollections.MuteWrapper
import social.firefly.core.model.Account
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.network.mastodon.MutesApi
import social.firefly.core.network.mastodon.utils.toMastodonPagedResponse
import social.firefly.core.repository.mastodon.model.status.toExternalModel

class MutesRepository(private val api: MutesApi, private val dao: MutesDao) {
    suspend fun getMutes(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
    ): MastodonPagedResponse<Account> = api.getMutes(
        maxId = maxId,
        sinceId = sinceId,
        minId = minId,
        limit = limit
    ).toMastodonPagedResponse { it.toExternalModel() }

    fun getPagingSource(): PagingSource<Int, MuteWrapper> = dao.pagingSource()

    suspend fun insertAll(databaseAccounts: List<DatabaseMute>) = dao.upsertAll(databaseAccounts)

    suspend fun deleteAll() = dao.deleteAll()
}
