package social.firefly.core.accounts

import androidx.room.Database
import androidx.room.RoomDatabase
import social.firefly.core.accounts.dao.ActiveAccountDao
import social.firefly.core.accounts.dao.MastodonAccountsDao
import social.firefly.core.accounts.model.ActiveAccount
import social.firefly.core.accounts.model.MastodonAccount

@Database(
    entities = [
        MastodonAccount::class,
        ActiveAccount::class,
    ],
    version = 1,
    autoMigrations = [

    ],
    exportSchema = true
)
internal abstract class AccountsDatabase : RoomDatabase() {
    abstract fun mastodonAccountsDao(): MastodonAccountsDao
    abstract fun activeAccountsDao(): ActiveAccountDao
}