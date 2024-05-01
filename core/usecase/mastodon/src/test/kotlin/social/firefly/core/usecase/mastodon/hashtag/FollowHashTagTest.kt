package social.firefly.core.usecase.mastodon.hashtag

import kotlinx.coroutines.test.TestScope
import social.firefly.core.usecase.mastodon.BaseUseCaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class FollowHashTagTest : BaseUseCaseTest() {
    private lateinit var subject: FollowHashTag

    @BeforeTest
    fun setup() {
        subject =
            FollowHashTag(
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
                hashtagRepository.followHashTag(hashTag)
            },
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                hashtagRepository.updateFollowing(any(), true)
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