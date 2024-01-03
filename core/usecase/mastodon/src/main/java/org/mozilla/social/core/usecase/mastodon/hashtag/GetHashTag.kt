package org.mozilla.social.core.usecase.mastodon.hashtag

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.mozilla.social.common.Resource
import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.core.model.HashTag
import org.mozilla.social.core.repository.mastodon.HashtagRepository
import timber.log.Timber

class GetHashTag(
    private val hashtagRepository: HashtagRepository,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {

    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(
        name: String,
        coroutineScope: CoroutineScope,
    ): Flow<Resource<HashTag>> =
        flow {
            emit(Resource.Loading())
            val deferred = CompletableDeferred<Resource<Unit>>()
            coroutineScope.launch(dispatcherIo) {
                try {
                    val hashtag = hashtagRepository.getHashTag(name)
                    hashtagRepository.insert(hashtag)
                    deferred.complete(
                        Resource.Loaded(
                            Unit
                        )
                    )
                } catch (e: Exception) {
                    Timber.e(e)
                    deferred.complete(Resource.Error(e))
                }
            }
            when (val deferredResult = deferred.await()) {
                is Resource.Error -> emit(Resource.Error(deferredResult.exception))
                else -> {
                    try {
                        emitAll(hashtagRepository.getHashTagFlow(name).map { Resource.Loaded(it) })
                    } catch (e: Exception) {
                        Timber.e(e)
                        emit(Resource.Error(e))
                    }
                }
            }
        }
}