package org.mozilla.social.core.domain.account

import kotlinx.coroutines.test.TestScope
import kotlinx.datetime.Instant
import org.mozilla.social.core.domain.BaseDomainTest
import org.mozilla.social.core.network.model.NetworkAccount
import kotlin.test.BeforeTest
import kotlin.test.Test

class UpdateMyAccountTest : BaseDomainTest() {

    private lateinit var subject: UpdateMyAccount

    private val networkAccountMock = NetworkAccount(
        accountId = "mediocritatem",
        username = "Rosie Small",
        acct = "eripuit",
        url = "https://duckduckgo.com/?q=laoreet",
        displayName = "Jeanie Dunlap",
        bio = "arcu",
        avatarUrl = "http://www.bing.com/search?q=populo",
        avatarStaticUrl = "https://duckduckgo.com/?q=ligula",
        headerUrl = "https://www.google.com/#q=convenire",
        headerStaticUrl = "http://www.bing.com/search?q=ne",
        isLocked = false,
        emojis = listOf(),
        createdAt = Instant.fromEpochMilliseconds(5000),
        lastStatusAt = null,
        statusesCount = 2056,
        followersCount = 8489,
        followingCount = 5952,
        isDiscoverable = null,
        movedTo = null,
        isGroup = false,
        fields = listOf(),
        isBot = null,
        source = null,
        isSuspended = null,
        muteExpiresAt = null
    )

    @BeforeTest
    fun setup() {
        subject = UpdateMyAccount(
            externalScope = TestScope(testDispatcher),
            showSnackbar = showSnackbar,
            accountApi = accountApi,
            socialDatabase = socialDatabase,
            dispatcherIo = testDispatcher,
        )
    }

    @Test
    fun testCancelledScope() {
        testOuterScopeCancelled(
            delayedCallBlock = {
                accountApi.updateAccount(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                )
            },
            delayedCallBlockReturnValue = networkAccountMock,
            subjectCallBlock = {
                subject()
            },
            verifyBlock = {
                accountsDao.insert(any())
            }
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                accountApi.updateAccount(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                )
            },
            subjectCallBlock = {
                subject()
            },
            verifyBlock = {
                showSnackbar(any(), any())
            },
        )
    }
}