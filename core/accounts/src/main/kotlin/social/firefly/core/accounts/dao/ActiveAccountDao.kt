package social.firefly.core.accounts.dao

import androidx.room.Query
import social.firefly.core.accounts.model.ActiveAccount

interface ActiveAccountDao : BaseDao<ActiveAccount> {

    @Query(
        "DELETE FROM activeAccount"
    )
    suspend fun removeActiveAccount()
}