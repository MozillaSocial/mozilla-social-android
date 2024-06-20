package social.firefly.core.accounts

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val accountsModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AccountsDatabase::class.java,
            "database-accounts",
        )
            .fallbackToDestructiveMigration()
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
    }

    single { get<AccountsDatabase>().mastodonAccountsDao() }
    single { get<AccountsDatabase>().activeAccountsDao() }
    singleOf(::AccountsManager)
}