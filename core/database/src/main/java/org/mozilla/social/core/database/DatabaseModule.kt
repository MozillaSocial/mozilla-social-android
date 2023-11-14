package org.mozilla.social.core.database

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.mozilla.social.core.database.dao.PollsDao
import org.mozilla.social.core.database.datasource.PollLocalDataSource

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            SocialDatabase::class.java, "database-social"
        ).build()
    }
    single { get<SocialDatabase>().pollDao() }
    single { PollLocalDataSource(get()) }
}