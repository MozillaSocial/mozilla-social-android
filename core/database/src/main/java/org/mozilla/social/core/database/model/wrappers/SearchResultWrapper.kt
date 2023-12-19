package org.mozilla.social.core.database.model.wrappers

import androidx.room.Embedded
import org.mozilla.social.core.database.model.entities.accountCollections.SearchedAccountWrapper
import org.mozilla.social.core.database.model.entities.hashtagCollections.SearchedHashTagWrapper
import org.mozilla.social.core.database.model.entities.statusCollections.SearchedStatusWrapper

data class SearchResultWrapper(
    @Embedded
    val searchedAccount: List<SearchedAccountWrapper>,
    @Embedded
    val searchedStatus: List<SearchedStatusWrapper>,
    @Embedded
    val searchedHashTag: List<SearchedHashTagWrapper>,
)