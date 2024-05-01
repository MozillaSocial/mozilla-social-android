package social.firefly.core.usecase.mastodon.status

import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import social.firefly.core.model.Status
import social.firefly.core.model.StatusVisibility
import social.firefly.core.test.fakes.Models
import social.firefly.core.usecase.mastodon.BaseUseCaseTest
import social.firefly.core.usecase.mastodon.thread.GetThread
import kotlin.test.BeforeTest
import kotlin.test.Test

class PostStatusTest : BaseUseCaseTest() {
    private lateinit var subject: PostStatus
    private val getThread = mockk<GetThread>(relaxed = true)

    private val networkStatus = Models.status

    @BeforeTest
    fun setup() {
        subject =
            PostStatus(
                externalScope = TestScope(testDispatcher),
                showSnackbar = showSnackbar,
                mediaApi = mediaRepository,
                statusRepository = statusRepository,
                timelineRepository = timelineRepository,
                getThread = getThread,
                dispatcherIo = testDispatcher,
                saveStatusToDatabase = saveStatusToDatabase,
            )
    }

    @Test
    fun testCancelledScope() {
        testOuterScopeCancelled(
            delayedCallBlock = {
                statusRepository.postStatus(any())
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
                saveStatusToDatabase(any<Status>())
            },
        )
    }

    @Test
    fun testCancelledScopeWithError() {
        testOuterScopeCancelledAndInnerException(
            delayedCallBlock = {
                statusRepository.postStatus(any())
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
            },
        )
    }
}
