package social.firefly.core.repository.mastodon.model.block

import social.firefly.core.database.model.entities.accountCollections.BlockWrapper
import social.firefly.core.database.model.entities.accountCollections.MuteWrapper
import social.firefly.core.model.wrappers.AccountAndRelationship
import social.firefly.core.repository.mastodon.model.account.toExternal
import social.firefly.core.repository.mastodon.model.status.toExternalModel

fun BlockWrapper.toExternal() = AccountAndRelationship(
    relationship = databaseRelationship.toExternal(),
    account = account.toExternalModel(),
)

fun MuteWrapper.toExternal() = AccountAndRelationship(
    relationship = databaseRelationship.toExternal(),
    account = account.toExternalModel(),
)
