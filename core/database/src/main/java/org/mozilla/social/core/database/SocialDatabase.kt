package org.mozilla.social.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.mozilla.social.core.database.converters.AttachmentConverter
import org.mozilla.social.core.database.converters.CardConverter
import org.mozilla.social.core.database.converters.EmojiConverter
import org.mozilla.social.core.database.converters.FieldConverter
import org.mozilla.social.core.database.converters.HashtagConverter
import org.mozilla.social.core.database.converters.HistoryConverter
import org.mozilla.social.core.database.converters.InstantConverter
import org.mozilla.social.core.database.converters.IntListConverter
import org.mozilla.social.core.database.converters.LocalDateConverter
import org.mozilla.social.core.database.converters.MentionConverter
import org.mozilla.social.core.database.converters.PollOptionConverter
import org.mozilla.social.core.database.dao.AccountTimelineStatusDao
import org.mozilla.social.core.database.dao.AccountsDao
import org.mozilla.social.core.database.dao.BlocksDao
import org.mozilla.social.core.database.dao.FavoritesTimelineStatusDao
import org.mozilla.social.core.database.dao.FederatedTimelineStatusDao
import org.mozilla.social.core.database.dao.FollowersDao
import org.mozilla.social.core.database.dao.FollowingsDao
import org.mozilla.social.core.database.dao.HashTagTimelineStatusDao
import org.mozilla.social.core.database.dao.HashTagsDao
import org.mozilla.social.core.database.dao.HomeTimelineStatusDao
import org.mozilla.social.core.database.dao.LocalTimelineStatusDao
import org.mozilla.social.core.database.dao.MutesDao
import org.mozilla.social.core.database.dao.NotificationsDao
import org.mozilla.social.core.database.dao.PollsDao
import org.mozilla.social.core.database.dao.RelationshipsDao
import org.mozilla.social.core.database.dao.SearchDao
import org.mozilla.social.core.database.dao.StatusDao
import org.mozilla.social.core.database.model.entities.DatabaseAccount
import org.mozilla.social.core.database.model.entities.DatabaseHashTagEntity
import org.mozilla.social.core.database.model.entities.DatabaseNotification
import org.mozilla.social.core.database.model.entities.DatabasePoll
import org.mozilla.social.core.database.model.entities.DatabaseRelationship
import org.mozilla.social.core.database.model.entities.DatabaseStatus
import org.mozilla.social.core.database.model.entities.accountCollections.Followee
import org.mozilla.social.core.database.model.entities.accountCollections.Follower
import org.mozilla.social.core.database.model.entities.statusCollections.AccountTimelineStatus
import org.mozilla.social.core.database.model.entities.statusCollections.FavoritesTimelineStatus
import org.mozilla.social.core.database.model.entities.statusCollections.FederatedTimelineStatus
import org.mozilla.social.core.database.model.entities.statusCollections.HashTagTimelineStatus
import org.mozilla.social.core.database.model.entities.statusCollections.HomeTimelineStatus
import org.mozilla.social.core.database.model.entities.statusCollections.LocalTimelineStatus
import org.mozilla.social.core.database.model.entities.accountCollections.DatabaseBlock
import org.mozilla.social.core.database.model.entities.accountCollections.DatabaseMute
import org.mozilla.social.core.database.model.entities.accountCollections.SearchedAccount
import org.mozilla.social.core.database.model.entities.hashtagCollections.SearchedHashTag
import org.mozilla.social.core.database.model.entities.notificationCollections.FollowListNotification
import org.mozilla.social.core.database.model.entities.notificationCollections.MainNotification
import org.mozilla.social.core.database.model.entities.notificationCollections.MentionListNotification
import org.mozilla.social.core.database.model.entities.statusCollections.SearchedStatus

@Suppress("MagicNumber")
@Database(
    entities = [
        DatabaseStatus::class,
        DatabaseBlock::class,
        DatabaseMute::class,
        DatabaseAccount::class,
        HomeTimelineStatus::class,
        DatabasePoll::class,
        HashTagTimelineStatus::class,
        AccountTimelineStatus::class,
        DatabaseRelationship::class,
        LocalTimelineStatus::class,
        FederatedTimelineStatus::class,
        Follower::class,
        Followee::class,
        FavoritesTimelineStatus::class,
        DatabaseHashTagEntity::class,
        SearchedAccount::class,
        SearchedStatus::class,
        SearchedHashTag::class,
        DatabaseNotification::class,
        MainNotification::class,
        MentionListNotification::class,
        FollowListNotification::class,
    ],
    version = 1,
    autoMigrations = [],
    exportSchema = true,
)
@TypeConverters(
    InstantConverter::class,
    LocalDateConverter::class,
    AttachmentConverter::class,
    EmojiConverter::class,
    FieldConverter::class,
    HashtagConverter::class,
    MentionConverter::class,
    HistoryConverter::class,
    PollOptionConverter::class,
    IntListConverter::class,
    CardConverter::class,
)
abstract class SocialDatabase : RoomDatabase() {
    abstract fun statusDao(): StatusDao

    abstract fun accountsDao(): AccountsDao

    abstract fun blocksDao(): BlocksDao

    abstract fun mutesDao(): MutesDao

    abstract fun homeTimelineDao(): HomeTimelineStatusDao

    abstract fun pollDao(): PollsDao

    abstract fun hashTagTimelineDao(): HashTagTimelineStatusDao

    abstract fun accountTimelineDao(): AccountTimelineStatusDao

    abstract fun relationshipsDao(): RelationshipsDao

    abstract fun localTimelineDao(): LocalTimelineStatusDao

    abstract fun federatedTimelineDao(): FederatedTimelineStatusDao

    abstract fun followersDao(): FollowersDao

    abstract fun followingsDao(): FollowingsDao

    abstract fun favoritesDao(): FavoritesTimelineStatusDao

    abstract fun hashTagsDao(): HashTagsDao

    abstract fun searchDao(): SearchDao

    abstract fun notificationsDao(): NotificationsDao
}
