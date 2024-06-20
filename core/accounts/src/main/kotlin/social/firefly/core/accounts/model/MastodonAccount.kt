package social.firefly.core.accounts.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "mastodonAccounts",
)
data class MastodonAccount(
    val accessToken: String,
    @PrimaryKey
    val accountId: String,
    val domain: String,
    val avatarUrl: String,
    val userName: String,
    val defaultLanguage: String,
    val serializedPushKeys: String? = null,
    val lastSeenHomeStatusId: String? = null,
)