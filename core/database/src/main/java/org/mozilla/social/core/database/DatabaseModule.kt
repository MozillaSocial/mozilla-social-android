package org.mozilla.social.core.database

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            SocialDatabase::class.java, "database-social"
        ).build()
    }
}