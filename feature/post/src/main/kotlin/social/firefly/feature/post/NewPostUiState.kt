package social.firefly.feature.post

import social.firefly.core.model.StatusVisibility
import social.firefly.post.bottombar.BottomBarState

data class NewPostUiState(
    val visibility: StatusVisibility = StatusVisibility.Public,
    val isSendingPost: Boolean = false,
    val isEditPost: Boolean = false,
    val sendButtonEnabled: Boolean = false,
    val bottomBarState: BottomBarState = BottomBarState(),
    val userHeaderState: UserHeaderState? = null,
)

data class UserHeaderState(val avatarUrl: String, val displayName: String)