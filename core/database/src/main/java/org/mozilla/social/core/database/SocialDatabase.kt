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
import org.mozilla.social.core.database.dao.FederatedTimelineStatusDao
import org.mozilla.social.core.database.dao.HashTagTimelineStatusDao
import org.mozilla.social.core.database.dao.HashtagDao
import org.mozilla.social.core.database.dao.HomeTimelineStatusDao
import org.mozilla.social.core.database.dao.LocalTimelineStatusDao
import org.mozilla.social.core.database.dao.PollsDao
import org.mozilla.social.core.database.dao.RelationshipsDao
import org.mozilla.social.core.database.dao.StatusDao
import org.mozilla.social.core.database.model.DatabaseAccount
import org.mozilla.social.core.database.model.DatabaseHashTag
import org.mozilla.social.core.database.model.DatabasePoll
import org.mozilla.social.core.database.model.DatabaseRelationship
import org.mozilla.social.core.database.model.DatabaseStatus
import org.mozilla.social.core.database.model.statusCollections.AccountTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.FederatedTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.HashTagTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.HomeTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.LocalTimelineStatus

@Suppress("MagicNumber")
@Database(
    entities = [
        DatabaseStatus::class,
        DatabaseAccount::class,
        DatabaseHashTag::class,
        HomeTimelineStatus::class,
        DatabasePoll::class,
        HashTagTimelineStatus::class,
        AccountTimelineStatus::class,
        DatabaseRelationship::class,
        LocalTimelineStatus::class,
        FederatedTimelineStatus::class,
    ],
    version = 8,
    autoMigrations = [
        AutoMigration(1, 2, DatabaseMigrations.Schema1to2::class),
        AutoMigration(2, 3),
        AutoMigration(3, 4),
        AutoMigration(4, 5),
        AutoMigration(5, 6),
        AutoMigration(6, 7),
        AutoMigration(7, 8, DatabaseMigrations.Schema7to8::class),
    ],
    exportSchema = true
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
    abstract fun hashtagDao(): HashtagDao
    abstract fun homeTimelineDao(): HomeTimelineStatusDao
    abstract fun pollDao(): PollsDao
    abstract fun hashTagTimelineDao(): HashTagTimelineStatusDao
    abstract fun accountTimelineDao(): AccountTimelineStatusDao
    abstract fun relationshipsDao(): RelationshipsDao
    abstract fun localTimelineDao(): LocalTimelineStatusDao
    abstract fun federatedTimelineDao(): FederatedTimelineStatusDao
}
