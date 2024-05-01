package social.firefly.core.database.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "trendingLinks",
)
data class TrendingLink(
    @PrimaryKey
    val url: String,
    val position: Int,
)