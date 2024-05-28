package social.firefly.core.repository.mastodon.model.block

import social.firefly.core.database.model.entities.accountCollections.BlockWrapper
import social.firefly.core.database.model.entities.accountCollections.MuteWrapper
import social.firefly.core.model.BlockedUser
import social.firefly.core.model.MutedUser
import social.firefly.core.repository.mastodon.model.account.toExternal
import social.firefly.core.repository.mastodon.model.status.toExternalModel

fun BlockWrapper.toBlockedUser() = BlockedUser(
    relationship = databaseRelationship.toExternal(),
    account = account.toExternalModel(),
)

fun MuteWrapper.toMutedUser() = MutedUser(
    isMuted = databaseRelationship.isMuting,
    account = account.toExternalModel(),
)