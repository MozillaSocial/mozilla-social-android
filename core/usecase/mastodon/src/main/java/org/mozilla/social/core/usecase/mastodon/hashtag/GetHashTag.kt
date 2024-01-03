package org.mozilla.social.core.usecase.mastodon.hashtag

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.mozilla.social.common.Resource
import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.core.model.HashTag
import org.mozilla.social.core.repository.mastodon.HashtagRepository
import timber.log.Timber

class GetHashTag(
    private val hashtagRepository: HashtagRepository,
) {

    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(
        name: String,
    ): Flow<Resource<HashTag>> =
        flow {
            emit(Resource.Loading())
            try {
                val hashtag = hashtagRepository.getHashTag(name)
                hashtagRepository.insert(hashtag)
                emitAll(hashtagRepository.getHashTagFlow(name).map { Resource.Loaded(it) })
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resource.Error(e))
            }
        }
}