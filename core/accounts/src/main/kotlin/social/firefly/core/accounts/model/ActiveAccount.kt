package social.firefly.core.accounts.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "activeAccount",
    foreignKeys = [
        ForeignKey(
            entity = MastodonAccount::class,
            parentColumns = ["accountId"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ]
)
data class ActiveAccount(
    @PrimaryKey
    val key: Int = 0,
    val accountType: AccountType,
    val accountId: String,
)

enum class AccountType {
    MASTODON
}