package org.mozilla.social.core.database

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule =
    module {
        single {
            Room.databaseBuilder(
                androidContext(),
                SocialDatabase::class.java,
                "database-social",
            ).build()
        }
        single { get<SocialDatabase>().pollDao() }
        single { get<SocialDatabase>().statusDao() }
        single { get<SocialDatabase>().accountsDao() }
        single { get<SocialDatabase>().followersDao() }
        single { get<SocialDatabase>().followingsDao() }
        single { get<SocialDatabase>().relationshipsDao() }
    }
