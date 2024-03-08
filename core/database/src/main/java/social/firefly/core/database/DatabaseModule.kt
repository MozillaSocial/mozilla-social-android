package social.firefly.core.database

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
            )
                .fallbackToDestructiveMigrationOnDowngrade()
                .build()
        }

        single { get<SocialDatabase>().accountsDao() }
        single { get<SocialDatabase>().accountTimelineDao() }
        single { get<SocialDatabase>().blocksDao() }
        single { get<SocialDatabase>().mutesDao() }
        single { get<SocialDatabase>().federatedTimelineDao() }
        single { get<SocialDatabase>().followersDao() }
        single { get<SocialDatabase>().followingsDao() }
        single { get<SocialDatabase>().hashTagTimelineDao() }
        single { get<SocialDatabase>().homeTimelineDao() }
        single { get<SocialDatabase>().localTimelineDao() }
        single { get<SocialDatabase>().pollDao() }
        single { get<SocialDatabase>().relationshipsDao() }
        single { get<SocialDatabase>().statusDao() }
        single { get<SocialDatabase>().favoritesDao() }
        single { get<SocialDatabase>().searchDao() }
        single { get<SocialDatabase>().hashTagsDao() }
        single { get<SocialDatabase>().notificationsDao() }
        single { get<SocialDatabase>().trendingHashtagDao() }
        single { get<SocialDatabase>().trendingLinkDao() }
        single { get<SocialDatabase>().trendingStatusDao() }
    }
