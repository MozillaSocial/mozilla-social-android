package org.mozilla.social.core.domain.status

import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.datetime.Instant
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.domain.BaseDomainTest
import org.mozilla.social.core.network.model.NetworkAccount
import org.mozilla.social.core.network.model.NetworkStatus
import org.mozilla.social.core.network.model.NetworkStatusVisibility
import kotlin.test.BeforeTest
import kotlin.test.Test

class BoostStatusTest : BaseDomainTest() {

    private val statusRepository = mockk<StatusRepository>(relaxed = true)

    private lateinit var subject: BoostStatus

    private val networkStatus = NetworkStatus(
        statusId = "vivendo",
        uri = "est",
        createdAt = Instant.fromEpochMilliseconds(5000),
        account = NetworkAccount(
            accountId = "nihil",
            username = "Caroline Meadows",
            acct = "legere",
            url = "https://www.google.com/#q=salutatus",
            displayName = "Celina Calhoun",
            bio = "inceptos",
            avatarUrl = "https://www.google.com/#q=atqui",
            avatarStaticUrl = "http://www.bing.com/search?q=himenaeos",
            headerUrl = "https://search.yahoo.com/search?p=detraxit",
            headerStaticUrl = "http://www.bing.com/search?q=cu",
            isLocked = false,
            emojis = listOf(),
            createdAt = Instant.fromEpochMilliseconds(5000),
            lastStatusAt = null,
            statusesCount = 1526,
            followersCount = 4322,
            followingCount = 3024,
            isDiscoverable = null,
            movedTo = null,
            isGroup = false,
            fields = listOf(),
            isBot = null,
            source = null,
            isSuspended = null,
            muteExpiresAt = null
        ),
        content = "qui",
        visibility = NetworkStatusVisibility.Public,
        isSensitive = false,
        contentWarningText = "conubia",
        mediaAttachments = listOf(),
        mentions = listOf(),
        hashTags = listOf(),
        emojis = listOf(),
        boostsCount = 3122,
        favouritesCount = 6279,
        repliesCount = 3980,
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
        subject = BoostStatus(
            externalScope = TestScope(testDispatcher),
            showSnackbar = showSnackbar,
            statusApi = statusApi,
            statusRepository = statusRepository,
            socialDatabase = socialDatabase,
            dispatcherIo = testDispatcher,
        )
    }

    @Test
    fun testCancelledScope() {
        testOuterScopeCancelled(
            delayedCallBlock = {
                statusApi.boostStatus(any())
            },
            delayedCallBlockReturnValue = networkStatus,
            subjectCallBlock = {
                subject("id")
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
                statusApi.boostStatus(any())
            },
            subjectCallBlock = {
                subject("id")
            },
            verifyBlock = {
                showSnackbar(any(), any())
            }
        )
    }
}