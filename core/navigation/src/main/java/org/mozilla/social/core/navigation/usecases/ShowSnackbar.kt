package org.mozilla.social.core.navigation.usecases

import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.navigation.Event
import org.mozilla.social.core.navigation.EventRelay

class ShowSnackbar(
    private val eventRelay: EventRelay,
) {
    operator fun invoke(
        text: StringFactory,
        isError: Boolean,
    ) {
        eventRelay.emitEvent(Event.ShowSnackbar(text = text, isError = isError))
    }
}
