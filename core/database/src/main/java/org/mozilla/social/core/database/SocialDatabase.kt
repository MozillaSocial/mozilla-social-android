package org.mozilla.social.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.mozilla.social.core.database.converters.AttachmentConverter
import org.mozilla.social.core.database.converters.EmojiConverter
import org.mozilla.social.core.database.converters.FieldConverter
import org.mozilla.social.core.database.converters.HashtagConverter
import org.mozilla.social.core.database.converters.HistoryConverter
import org.mozilla.social.core.database.converters.InstantConverter
import org.mozilla.social.core.database.converters.IntListConverter
import org.mozilla.social.core.database.converters.LocalDateConverter
import org.mozilla.social.core.database.converters.MentionConverter
import org.mozilla.social.core.database.converters.PollOptionConverter
import org.mozilla.social.core.database.dao.AccountsDao
import org.mozilla.social.core.database.dao.HashTagTimelineStatusDao
import org.mozilla.social.core.database.dao.HashtagDao
import org.mozilla.social.core.database.dao.HomeTimelineStatusDao
import org.mozilla.social.core.database.dao.PollsDao
import org.mozilla.social.core.database.dao.StatusDao
import org.mozilla.social.core.database.model.DatabaseAccount
import org.mozilla.social.core.database.model.DatabaseHashTag
import org.mozilla.social.core.database.model.DatabasePoll
import org.mozilla.social.core.database.model.DatabaseStatus
import org.mozilla.social.core.database.model.statusCollections.HashTagTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.HomeTimelineStatus

@Database(
    entities = [
        DatabaseStatus::class,
        DatabaseAccount::class,
        DatabaseHashTag::class,
        HomeTimelineStatus::class,
        DatabasePoll::class,
        HashTagTimelineStatus::class,
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(1, 2, DatabaseMigrations.Schema1to2::class)
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
)
abstract class SocialDatabase : RoomDatabase() {
    abstract fun statusDao(): StatusDao
    abstract fun accountsDao(): AccountsDao
    abstract fun hashtagDao(): HashtagDao
    abstract fun homeTimelineDao(): HomeTimelineStatusDao
    abstract fun pollDao(): PollsDao
    abstract fun hashTagTimelineDao(): HashTagTimelineStatusDao
}
