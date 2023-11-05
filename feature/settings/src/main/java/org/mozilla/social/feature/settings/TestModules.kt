package org.mozilla.social.feature.settings

import org.koin.dsl.module
import org.mozilla.social.core.navigation.EventRelay
import org.mozilla.social.core.navigation.usecases.PopNavBackstack

// I'm not sure the best place to put these
val previewModule = module {
    single { PopNavBackstack(EventRelay()) }
}