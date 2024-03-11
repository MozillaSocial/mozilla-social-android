package social.firefly.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import social.firefly.core.database.converters.AttachmentConverter
import social.firefly.core.database.converters.CardConverter
import social.firefly.core.database.converters.EmojiConverter
import social.firefly.core.database.converters.FieldConverter
import social.firefly.core.database.converters.HashtagConverter
import social.firefly.core.database.converters.HistoryConverter
import social.firefly.core.database.converters.InstantConverter
import social.firefly.core.database.converters.IntListConverter
import social.firefly.core.database.converters.LocalDateConverter
import social.firefly.core.database.converters.MentionConverter
import social.firefly.core.database.converters.PollOptionConverter
import social.firefly.core.database.dao.AccountTimelineStatusDao
import social.firefly.core.database.dao.AccountsDao
import social.firefly.core.database.dao.BlocksDao
import social.firefly.core.database.dao.FavoritesTimelineStatusDao
import social.firefly.core.database.dao.FederatedTimelineStatusDao
import social.firefly.core.database.dao.FollowersDao
import social.firefly.core.database.dao.FollowingsDao
import social.firefly.core.database.dao.HashTagTimelineStatusDao
import social.firefly.core.database.dao.HashTagsDao
import social.firefly.core.database.dao.HomeTimelineStatusDao
import social.firefly.core.database.dao.LocalTimelineStatusDao
import social.firefly.core.database.dao.MutesDao
import social.firefly.core.database.dao.NotificationsDao
import social.firefly.core.database.dao.PollsDao
import social.firefly.core.database.dao.RelationshipsDao
import social.firefly.core.database.dao.SearchDao
import social.firefly.core.database.dao.StatusDao
import social.firefly.core.database.dao.TrendingHashTagDao
import social.firefly.core.database.dao.TrendingLinkDao
import social.firefly.core.database.dao.TrendingStatusDao
import social.firefly.core.database.model.entities.DatabaseAccount
import social.firefly.core.database.model.entities.DatabaseHashTagEntity
import social.firefly.core.database.model.entities.DatabaseNotification
import social.firefly.core.database.model.entities.DatabasePoll
import social.firefly.core.database.model.entities.DatabaseRelationship
import social.firefly.core.database.model.entities.DatabaseStatus
import social.firefly.core.database.model.entities.TrendingLink
import social.firefly.core.database.model.entities.accountCollections.DatabaseBlock
import social.firefly.core.database.model.entities.accountCollections.DatabaseMute
import social.firefly.core.database.model.entities.accountCollections.Followee
import social.firefly.core.database.model.entities.accountCollections.Follower
import social.firefly.core.database.model.entities.accountCollections.SearchedAccount
import social.firefly.core.database.model.entities.hashtagCollections.SearchedHashTag
import social.firefly.core.database.model.entities.hashtagCollections.TrendingHashTag
import social.firefly.core.database.model.entities.notificationCollections.FollowListNotification
import social.firefly.core.database.model.entities.notificationCollections.MainNotification
import social.firefly.core.database.model.entities.notificationCollections.MentionListNotification
import social.firefly.core.database.model.entities.statusCollections.AccountTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.FavoritesTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.FederatedTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.HashTagTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.HomeTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.LocalTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.SearchedStatus
import social.firefly.core.database.model.entities.statusCollections.DbTrendingStatus

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
        TrendingLink::class,
        DbTrendingStatus::class,
        TrendingHashTag::class,
    ],
    version = 2,
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ],
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

    abstract fun trendingLinkDao(): TrendingLinkDao

    abstract fun trendingHashtagDao(): TrendingHashTagDao

    abstract fun trendingStatusDao(): TrendingStatusDao

    abstract fun notificationsDao(): NotificationsDao
}
