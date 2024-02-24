package social.firefly.feature.account

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import social.firefly.core.model.AccountTimelineType
import social.firefly.core.ui.common.following.FollowStatus
import social.firefly.core.ui.postcard.PostCardUiState

data class AccountUiState(
    val accountId: String,
    val username: String,
    val webFinger: String,
    val displayName: String,
    val accountUrl: String,
    val bio: String,
    val avatarUrl: String,
    val headerUrl: String,
    val followersCount: Long,
    val followingCount: Long,
    val statusesCount: Long,
    val fields: List<AccountFieldUiState>,
    val isBot: Boolean,
    val followStatus: FollowStatus,
    val isMuted: Boolean,
    val isBlocked: Boolean,
    val joinDate: LocalDateTime,
)

data class AccountFieldUiState(
    val name: String,
    val value: String,
)

data class Timeline(
    val type: AccountTimelineType,
    val postsFeed: Flow<PagingData<PostCardUiState>>,
    val postsAndRepliesFeed: Flow<PagingData<PostCardUiState>>,
    val mediaFeed: Flow<PagingData<PostCardUiState>>,
)
