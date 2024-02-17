package social.firefly.core.navigation.usecases

import social.firefly.common.utils.StringFactory
import social.firefly.core.navigation.Event
import social.firefly.core.navigation.EventRelay

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
