package org.mozilla.social.core.ui.postcard

import org.koin.dsl.module

val postCardModule = module {
    factory { parametersHolder ->
        PostCardDelegate(
            coroutineScope = parametersHolder[0],
            statusRepository = get(),
            navigateTo = get(),
            openLink = get(),
            showSnackbar = get(),
            blockAccount = get(),
            muteAccount = get(),
            voteOnPoll = get(),
            boostStatus = get(),
            undoBoostStatus = get(),
            favoriteStatus = get(),
            undoFavoriteStatus = get(),
        )
    }
}