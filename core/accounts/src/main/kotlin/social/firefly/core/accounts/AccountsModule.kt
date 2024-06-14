package social.firefly.core.accounts

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val accountsModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AccountsDatabase::class.java,
            "database-social",
        )
            .fallbackToDestructiveMigration()
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
    }

    single { get<AccountsDatabase>().mastodonAccountsDao() }
}