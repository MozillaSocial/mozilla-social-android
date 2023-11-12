package org.mozilla.social.core.usecase.mastodon.status

import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.storage.mastodon.timeline.DeleteStatusFailedException
import org.mozilla.social.core.storage.mastodon.timeline.LocalTimelineRepository
import org.mozilla.social.core.usecase.mastodon.R

/**
 * Deletes the status from the locally stored timelines
 */
class DeleteStatusFromTimelines(
    private val localTimelineRepository: LocalTimelineRepository,
    private val showSnackbar: ShowSnackbar,
) {

    suspend operator fun invoke(statusId: String) {
        try {
            localTimelineRepository.deleteFromTimelines(statusId = statusId)
        } catch (exception: DeleteStatusFailedException) {
            showSnackbar(
                text = StringFactory.resource(R.string.error_deleting_post),
                isError = true,
            )
        }
    }
}