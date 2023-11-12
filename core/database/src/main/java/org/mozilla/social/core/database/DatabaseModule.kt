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
    single { get<SocialDatabase>().accountsDao() }
    single { get<SocialDatabase>().statusDao() }
    single { get<SocialDatabase>().hashtagDao() }
    single { get<SocialDatabase>().homeTimelineDao() }
    single { get<SocialDatabase>().pollDao() }
    single { get<SocialDatabase>().hashTagTimelineDao() }
    single { get<SocialDatabase>().accountTimelineDao() }
    single { get<SocialDatabase>().relationshipsDao() }
    single { get<SocialDatabase>().localTimelineDao() }
    single { get<SocialDatabase>().federatedTimelineDao() }
}