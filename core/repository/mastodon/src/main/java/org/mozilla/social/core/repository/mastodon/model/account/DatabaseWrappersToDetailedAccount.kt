package org.mozilla.social.core.repository.mastodon.model.account

import org.mozilla.social.core.database.model.entities.accountCollections.FolloweeWrapper
import org.mozilla.social.core.database.model.entities.accountCollections.FollowerWrapper
import org.mozilla.social.core.database.model.entities.accountCollections.SearchedAccountWrapper
import org.mozilla.social.core.model.wrappers.DetailedAccountWrapper
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel

fun FollowerWrapper.toDetailedAccount(): DetailedAccountWrapper =
    DetailedAccountWrapper(
        account = followerAccount.toExternalModel(),
        relationship = relationship.toExternal(),
    )

fun FolloweeWrapper.toDetailedAccount(): DetailedAccountWrapper =
    DetailedAccountWrapper(
        account = followingAccount.toExternalModel(),
        relationship = relationship.toExternal(),
    )

fun SearchedAccountWrapper.toDetailedAccount(): DetailedAccountWrapper =
    DetailedAccountWrapper(
        account = account.toExternalModel(),
        relationship = databaseRelationship.toExternal(),
    )
