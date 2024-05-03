package social.firefly.core.datastore

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

internal class UserPreferencesSerializer(
    domain: String,
    accessToken: String,
    accountId: String,
) : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences.newBuilder()
        .setDomain(domain)
        .setAccessToken(accessToken)
        .setAccountId(accountId)
        .setThreadType(UserPreferences.ThreadType.TREE)
        .build()

    override suspend fun readFrom(input: InputStream): UserPreferences =
        UserPreferences.parseFrom(input)

    override suspend fun writeTo(
        t: UserPreferences,
        output: OutputStream,
    ) = t.writeTo(output)
}

internal object EmptyUserPreferencesSerializer : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences.newBuilder()
        .build()

    override suspend fun readFrom(input: InputStream): UserPreferences =
        UserPreferences.parseFrom(input)

    override suspend fun writeTo(
        t: UserPreferences,
        output: OutputStream,
    ) = t.writeTo(output)
}
