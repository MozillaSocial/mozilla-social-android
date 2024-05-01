package social.firefly.core.repository.mastodon.model.account

import social.firefly.core.database.model.entities.accountCollections.FolloweeWrapper
import social.firefly.core.database.model.entities.accountCollections.FollowerWrapper
import social.firefly.core.database.model.entities.accountCollections.SearchedAccountWrapper
import social.firefly.core.model.wrappers.DetailedAccountWrapper
import social.firefly.core.repository.mastodon.model.status.toExternalModel

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
