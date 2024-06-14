package social.firefly.core.accounts.dao

import androidx.room.Dao
import social.firefly.core.accounts.model.MastodonAccount

@Dao
interface MastodonAccountsDao : BaseDao<MastodonAccount> {
}