package social.firefly.core.datastore

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.File

class UserPreferencesDatastoreManager(
    private val context: Context,
    private val appPreferencesDatastore: AppPreferencesDatastore,
) {
    private val dataStores: MutableList<UserPreferencesDatastore> = mutableListOf()
    val isLoggedInToAtLeastOneAccount: Boolean
        get() = dataStores.isNotEmpty()

    // counter exists to ensure we don't create a datastore preferences with a name that already exists.
    // this could happen if the user logs out and logs back in with the same account.
    // That would cause a crash becauses two datastores with the same name can't exist at the same time,
    // and even though we delete the datastore, it might be lurking in memory somewhere until
    // the user logs in.
    private var counter = 0

    init {
        removeLegacyUserPreferences()
        val dataStoreFileNames = DatastoreUtils.getAllUserPreferencesDatastoreFilesNames(context)
        dataStoreFileNames.forEach { fileName ->
            dataStores.add(
                UserPreferencesDatastore(
                    fileName = fileName,
                    EmptyUserPreferencesSerializer,
                    context = context,
                )
            )
        }
    }

    val activeUserDatastore: Flow<UserPreferencesDatastore> =
        appPreferencesDatastore.activeUserDatastoreFilename.map { activeDatastoreId ->
            dataStores.find { it.fileName == activeDatastoreId }
        }.filterNotNull()

    suspend fun createNewUserDatastore(
        domain: String,
        accessToken: String,
        accountId: String,
    ) {
        require(UserPreferencesDatastore.HOST_NAME_REGEX.toRegex().matches(domain))
        val fileName = "$domain-$accountId-$counter-prefs.pb"
        counter++

        if (dataStores.find { it.fileName == fileName } != null)
            throw Exception("prefs file already exists")

        dataStores.add(
            UserPreferencesDatastore(
                fileName = fileName,
                serializer = UserPreferencesSerializer(
                    domain = domain,
                    accessToken = accessToken,
                    accountId = accountId,
                ),
                context = context,
            )
        )

        appPreferencesDatastore.saveActiveUserDatastoreFilename(fileName)
    }

    suspend fun deleteDataStore(
        dataStore: UserPreferencesDatastore,
    ) {
        // if we are deleting the current user account, update the active user
        if (appPreferencesDatastore.activeUserDatastoreFilename.first() == dataStore.fileName) {
            dataStores.filterNot { it == dataStore }.firstOrNull()?.let {
                appPreferencesDatastore.saveActiveUserDatastoreFilename(it.fileName)
            } ?: appPreferencesDatastore.saveActiveUserDatastoreFilename("")
        }
        val datastoreDir = File(context.filesDir, "datastore")
        val dataStoreFile = File(datastoreDir, dataStore.fileName)
        dataStoreFile.delete()
        dataStores.remove(dataStore)
    }

    private fun removeLegacyUserPreferences() {
        val datastoreDir = File(context.filesDir, "datastore")
        val dataStoreFile = File(datastoreDir, "userPreferences.pb")
        if (dataStoreFile.exists()) {
            dataStoreFile.delete()
        }
    }
}

