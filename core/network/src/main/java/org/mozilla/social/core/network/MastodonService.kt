package org.mozilla.social.core.network

import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.auth.AuthToken
import fr.outadoc.mastodonk.auth.AuthTokenProvider
import fr.outadoc.mastodonk.client.MastodonClient
import org.mozilla.social.core.network.media.toDomain
import org.mozilla.social.core.network.media.toMastodonk
import org.mozilla.social.model.Page
import org.mozilla.social.model.PageInfo
import org.mozilla.social.model.Status
import org.mozilla.social.model.entity.Attachment
import org.mozilla.social.model.entity.request.File

class MastodonService(accessToken: String) {

    private var client = MastodonClient {
        domain = "mozilla.social"
        authTokenProvider = AuthTokenProvider {
            // Provide an authentication token
            AuthToken(accessToken = accessToken)
        }
    }

    suspend fun getPublicTimeline(): Page<List<Status>> =
        client.timelines.getPublicTimeline().toDomain()

    suspend fun getLocalTimeline(): Page<List<Status>> =
        client.timelines.getHomeTimeline().toDomain()


    private fun fr.outadoc.mastodonk.api.entity.paging.Page<List<fr.outadoc.mastodonk.api.entity.Status>>.toDomain(): Page<List<Status>> {
        return Page(
                contents = contents.map { it.toDomain() },
                nextPage = nextPage.toDomain(),
                previousPage = previousPage.toDomain()
            )
        }

    suspend fun uploadImage(
        file: File,
        description: String?
    ): Attachment {
        return client.media.uploadMediaAttachment(
            file = file.toMastodonk(),
            thumbnail = null,
            description = description,
            focus = "0.0,0.0",
        ).toDomain()
    }
}

private fun fr.outadoc.mastodonk.api.entity.Status.toDomain(): Status {
    return Status(statusId, uri, account.toDomain(), content)
}

private fun Account.toDomain(): org.mozilla.social.model.Account =
    org.mozilla.social.model.Account(accountId, username)

private fun fr.outadoc.mastodonk.api.entity.paging.PageInfo?.toDomain(): PageInfo? {
    return if (this == null) null else PageInfo(sinceId, minId, maxId)
}
