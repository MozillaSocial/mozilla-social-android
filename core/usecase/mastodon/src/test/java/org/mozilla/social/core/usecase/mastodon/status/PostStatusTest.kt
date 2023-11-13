package org.mozilla.social.core.usecase.mastodon.status

import kotlinx.coroutines.test.TestScope
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.usecase.mastodon.BaseUseCaseTest
import org.mozilla.social.core.test.fakes.Models
import org.mozilla.social.core.model.StatusVisibility
import kotlin.test.BeforeTest
import kotlin.test.Test

class PostStatusTest : BaseUseCaseTest() {
    private lateinit var subject: PostStatus

    private val networkStatus = Models.status

    @BeforeTest
    fun setup() {
        subject = PostStatus(
            externalScope = TestScope(testDispatcher),
            showSnackbar = showSnackbar,
            mediaApi = mediaRepository,
            statusRepository = statusRepository,
            timelineRepository = timelineRepository,
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
                saveStatusToDatabase(any<List<Status>>())
            }
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
            }
        )
    }
}