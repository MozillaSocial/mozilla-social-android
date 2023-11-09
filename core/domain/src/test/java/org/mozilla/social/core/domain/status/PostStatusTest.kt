package org.mozilla.social.core.domain.status

import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.datetime.Instant
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.data.repository.TimelineRepository
import org.mozilla.social.core.domain.BaseDomainTest
import org.mozilla.social.core.network.model.NetworkAccount
import org.mozilla.social.core.network.model.NetworkStatus
import org.mozilla.social.core.network.model.NetworkStatusVisibility
import org.mozilla.social.model.StatusVisibility
import kotlin.test.BeforeTest
import kotlin.test.Test

class PostStatusTest : BaseDomainTest() {

    private val statusRepository = mockk<StatusRepository>(relaxed = true)
    private val timelineRepository = mockk<TimelineRepository>(relaxed = true)

    private lateinit var subject: PostStatus

    private val networkStatus = NetworkStatus(
        statusId = "te",
        uri = "dolor",
        createdAt = Instant.fromEpochMilliseconds(5000),
        account = NetworkAccount(
            accountId = "suscipiantur",
            username = "Christina Garcia",
            acct = "labores",
            url = "https://search.yahoo.com/search?p=partiendo",
            displayName = "Carmela Hodges",
            bio = "an",
            avatarUrl = "http://www.bing.com/search?q=affert",
            avatarStaticUrl = "https://search.yahoo.com/search?p=sumo",
            headerUrl = "https://search.yahoo.com/search?p=tamquam",
            headerStaticUrl = "http://www.bing.com/search?q=vel",
            isLocked = false,
            emojis = listOf(),
            createdAt = Instant.fromEpochMilliseconds(5000),
            lastStatusAt = null,
            statusesCount = 1571,
            followersCount = 1602,
            followingCount = 4511,
            isDiscoverable = null,
            movedTo = null,
            isGroup = false,
            fields = listOf(),
            isBot = null,
            source = null,
            isSuspended = null,
            muteExpiresAt = null
        ),
        content = "posse",
        visibility = NetworkStatusVisibility.Public,
        isSensitive = false,
        contentWarningText = "doming",
        mediaAttachments = listOf(),
        mentions = listOf(),
        hashTags = listOf(),
        emojis = listOf(),
        boostsCount = 2184,
        favouritesCount = 1440,
        repliesCount = 6170,
        application = null,
        url = null,
        inReplyToId = null,
        inReplyToAccountId = null,
        boostedStatus = null,
        poll = null,
        card = null,
        language = null,
        plainText = null,
        isFavourited = null,
        isBoosted = null,
        isMuted = null,
        isBookmarked = null,
        isPinned = null
    )

    @BeforeTest
    fun setup() {
        subject = PostStatus(
            externalScope = TestScope(testDispatcher),
            showSnackbar = showSnackbar,
            statusApi = statusApi,
            mediaApi = mediaApi,
            statusRepository = statusRepository,
            timelineRepository = timelineRepository,
            dispatcherIo = testDispatcher,
        )
    }

    @Test
    fun testCancelledScope() {
        testOuterScopeCancelled(
            delayedCallBlock = {
                statusApi.postStatus(any())
            },
            delayedCallBlockReturnValue = networkStatus,
            subjectCallBlock = {
                subject(
                    statusText = "test",
                    imageStates = emptyList(),
                    visibility = StatusVisibility.Public,
                    pollCreate = null,
                    contentWarningText = null,
                    inReplyToId = null,
                )
            },
            verifyBlock = {
                statusRepository.saveStatusToDatabase(any())
            }
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                statusApi.postStatus(any())
            },
            subjectCallBlock = {
                subject(
                    statusText = "test",
                    imageStates = emptyList(),
                    visibility = StatusVisibility.Public,
                    pollCreate = null,
                    contentWarningText = null,
                    inReplyToId = null,
                )
            },
            verifyBlock = {
                showSnackbar(any(), any())
            }
        )
    }
}