package social.firefly.core.accounts.model

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "mastodonAccounts",
    primaryKeys = ["accountId", "domain"]
)
data class MastodonAccount(
    val accessToken: String,
    val accountId: String,
    val domain: String,
    val avatarUrl: String,
    val userName: String,
    val defaultLanguage: String,
    val serializedPushKeys: String?,
    val lastSeenHomeStatusId: String?,
)