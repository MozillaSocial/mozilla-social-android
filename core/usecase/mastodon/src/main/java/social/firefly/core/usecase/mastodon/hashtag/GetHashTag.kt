package social.firefly.core.usecase.mastodon.hashtag

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
import social.firefly.common.Resource
import social.firefly.common.annotations.PreferUseCase
import social.firefly.core.model.HashTag
import social.firefly.core.repository.mastodon.HashtagRepository
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
            // the hashtag from the server might be lower case, so we need to assign this
            // to whatever we get back from the server
            var realName: String = name
            coroutineScope.launch(dispatcherIo) {
                try {
                    val hashtag = hashtagRepository.getHashTag(realName)
                    realName = hashtag.name
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
                        emitAll(hashtagRepository.getHashTagFlow(realName).map { Resource.Loaded(it) })
                    } catch (e: Exception) {
                        Timber.e(e)
                        emit(Resource.Error(e))
                    }
                }
            }
        }
}