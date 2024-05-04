package social.firefly.core.datastore

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File

class UserPreferencesDatastoreManager(
    private val context: Context,
    private val appPreferencesDatastore: AppPreferencesDatastore,
) {
    private val dataStores: MutableList<UserPreferencesDatastore> = mutableListOf()

    init {
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

    private val dummyDataStore = UserPreferencesDatastore(
        DUMMY_FILENAME,
        EmptyUserPreferencesSerializer,
        context
    )

    val activeUserDatastore: Flow<UserPreferencesDatastore> =
        appPreferencesDatastore.activeUserDatastoreFilename.map { activeDatastoreId ->
            dataStores.find { it.fileName == activeDatastoreId } ?: dummyDataStore
        }

    suspend fun createNewUserDatastore(
        domain: String,
        accessToken: String,
        accountId: String,
    ) {
        require(UserPreferencesDatastore.HOST_NAME_REGEX.toRegex().matches(domain))
        val fileName = "$accountId-prefs.pb"

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

    fun deleteDataStore(
        dataStore: UserPreferencesDatastore,
    ) {
        if (dataStore.fileName == DUMMY_FILENAME) return
        val datastoreDir = File(context.filesDir, "datastore")
        val dataStoreFile = File(datastoreDir, dataStore.fileName)
        dataStoreFile.delete()
    }

    companion object {
        const val DUMMY_FILENAME = "dummy.pb"
    }
}

