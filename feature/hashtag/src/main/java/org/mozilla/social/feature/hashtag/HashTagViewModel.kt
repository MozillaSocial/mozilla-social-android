package org.mozilla.social.feature.hashtag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.map
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.model.statusCollections.toStatusWrapper
import org.mozilla.social.core.domain.GetLoggedInUserAccountId
import org.mozilla.social.core.domain.remotemediators.HashTagTimelineRemoteMediator
import org.mozilla.social.core.ui.postcard.PostCardDelegate
import org.mozilla.social.core.ui.postcard.toPostCardUiState

class HashTagViewModel(
    hashTag: String,
    socialDatabase: SocialDatabase,
    userAccountId: GetLoggedInUserAccountId,
) : ViewModel() {

    private val hashTagTimelineRemoteMediator: HashTagTimelineRemoteMediator by inject(
        HashTagTimelineRemoteMediator::class.java
    ) { parametersOf(hashTag) }

    @OptIn(ExperimentalPagingApi::class)
    val feed = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40
        ),
        remoteMediator = hashTagTimelineRemoteMediator
    ) {
        socialDatabase.hashTagTimelineDao().hashTagTimelinePagingSource(hashTag)
    }.flow.map { pagingData ->
        pagingData.map {
            it.toStatusWrapper().toExternalModel().toPostCardUiState(userAccountId())
        }
    }.cachedIn(viewModelScope)

    val postCardDelegate: PostCardDelegate by inject(
        PostCardDelegate::class.java
    ) { parametersOf(viewModelScope) }
}