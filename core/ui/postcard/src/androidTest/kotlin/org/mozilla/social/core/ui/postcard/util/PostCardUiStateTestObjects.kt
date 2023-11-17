package org.mozilla.social.core.ui.postcard.util

import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.ui.postcard.MainPostCardUiState
import org.mozilla.social.core.ui.postcard.PostCardUiState
import org.mozilla.social.core.ui.postcard.TopRowIconType
import org.mozilla.social.core.ui.postcard.TopRowMetaDataUiState

object PostCardUiStateTestObject1 {

    val statusId = "123"

    val test1 = PostCardUiState(
        statusId,
        topRowMetaDataUiState = TopRowMetaDataUiState(
            iconType = TopRowIconType.BOOSTED,
            text = StringFactory.literal("test")
        ),
        mainPostCardUiState = MainPostCardUiState(
            url = "mozilla.social/me",
            pollUiState = null,
            username = "Cool Guy",
            statusTextHtml = "I'm a really cool guy didn't you know?",
            mediaAttachments = emptyList(),
            profilePictureUrl = "",
            postTimeSince = StringFactory.literal("1 minute ago"),
            accountName = StringFactory.literal("@coolguy"),
            replyCount = 0L,
            boostCount = 0L,
            favoriteCount = 0L,
            statusId = statusId,
            userBoosted = false,
            isFavorited = false,
            accountId = "456",
            mentions = emptyList(),
            previewCard = null,
            isUsersPost = false,
            isBeingDeleted = false,
            contentWarning = ""
        )
    )
}