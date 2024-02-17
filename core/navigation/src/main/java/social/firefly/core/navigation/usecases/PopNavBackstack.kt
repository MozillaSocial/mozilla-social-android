package social.firefly.core.navigation.usecases

import social.firefly.core.navigation.Event
import social.firefly.core.navigation.EventRelay

class PopNavBackstack(
    private val eventRelay: EventRelay,
) {
    operator fun invoke() {
        eventRelay.emitEvent(Event.PopBackStack)
    }
}
