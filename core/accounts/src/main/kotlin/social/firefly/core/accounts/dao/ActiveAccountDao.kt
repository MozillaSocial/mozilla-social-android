package social.firefly.core.accounts.dao

import androidx.room.Dao
import androidx.room.Query
import social.firefly.core.accounts.model.ActiveAccount

@Dao
interface ActiveAccountDao : BaseDao<ActiveAccount> {

    @Query(
        "DELETE FROM activeAccount"
    )
    suspend fun removeActiveAccount()
}