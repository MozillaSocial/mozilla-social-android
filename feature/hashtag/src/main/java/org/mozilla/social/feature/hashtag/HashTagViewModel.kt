package org.mozilla.social.feature.hashtag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.ui.postcard.PostCardDelegate
import org.mozilla.social.core.ui.postcard.PostCardNavigation

class HashTagViewModel(
    statusRepository: StatusRepository,
    accountRepository: AccountRepository,
    log: Log,
    hastTag: String,
    postCardNavigation: PostCardNavigation,
) : ViewModel() {

    val postCardDelegate = PostCardDelegate(
        coroutineScope = viewModelScope,
        statusRepository = statusRepository,
        accountRepository = accountRepository,
        log = log,
        postCardNavigation = postCardNavigation,
    )
}