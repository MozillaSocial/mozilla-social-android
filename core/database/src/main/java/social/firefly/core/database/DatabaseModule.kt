package social.firefly.core.database

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule =
    module {
        single {
            Room.databaseBuilder(
                androidContext(),
                social.firefly.core.database.SocialDatabase::class.java,
                "database-social",
            )
                .fallbackToDestructiveMigrationOnDowngrade()
                .build()
        }

        single { get<social.firefly.core.database.SocialDatabase>().accountsDao() }
        single { get<social.firefly.core.database.SocialDatabase>().accountTimelineDao() }
        single { get<social.firefly.core.database.SocialDatabase>().blocksDao() }
        single { get<social.firefly.core.database.SocialDatabase>().mutesDao() }
        single { get<social.firefly.core.database.SocialDatabase>().federatedTimelineDao() }
        single { get<social.firefly.core.database.SocialDatabase>().followersDao() }
        single { get<social.firefly.core.database.SocialDatabase>().followingsDao() }
        single { get<social.firefly.core.database.SocialDatabase>().hashTagTimelineDao() }
        single { get<social.firefly.core.database.SocialDatabase>().homeTimelineDao() }
        single { get<social.firefly.core.database.SocialDatabase>().localTimelineDao() }
        single { get<social.firefly.core.database.SocialDatabase>().pollDao() }
        single { get<social.firefly.core.database.SocialDatabase>().relationshipsDao() }
        single { get<social.firefly.core.database.SocialDatabase>().statusDao() }
        single { get<social.firefly.core.database.SocialDatabase>().favoritesDao() }
        single { get<social.firefly.core.database.SocialDatabase>().searchDao() }
        single { get<social.firefly.core.database.SocialDatabase>().hashTagsDao() }
        single { get<social.firefly.core.database.SocialDatabase>().notificationsDao() }
    }
