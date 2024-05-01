package social.firefly.core.usecase.mastodon.hashtag

import kotlinx.coroutines.test.TestScope
import social.firefly.core.usecase.mastodon.BaseUseCaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class UnfollowHashTagTest : BaseUseCaseTest() {
    private lateinit var subject: UnfollowHashTag

    @BeforeTest
    fun setup() {
        subject =
            UnfollowHashTag(
                externalScope = TestScope(testDispatcher),
                showSnackbar = showSnackbar,
                dispatcherIo = testDispatcher,
                hashtagRepository = hashtagRepository
            )
    }

    @Test
    fun testCancelledScope() {
        val hashTag = "tag"
        testOuterScopeCancelled(
            delayedCallBlock = {
                hashtagRepository.updateFollowing(any(), any())
            },
            subjectCallBlock = {
                subject(hashTag)
            },
            verifyBlock = {
                hashtagRepository.unfollowHashTag(hashTag)
            },
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                hashtagRepository.updateFollowing(any(), false)
            },
            subjectCallBlock = {
                subject("tag")
            },
            verifyBlock = {
                showSnackbar(any(), any())
            },
        )
    }
}