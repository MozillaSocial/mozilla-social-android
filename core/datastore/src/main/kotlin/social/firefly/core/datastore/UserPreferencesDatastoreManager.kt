package social.firefly.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.core.okio.OkioSerializer
import androidx.datastore.core.okio.OkioStorage
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import okio.BufferedSink
import okio.BufferedSource
import okio.FileSystem
import okio.Path.Companion.toPath
import java.io.File

class UserPreferencesDatastoreManager(
    private val context: Context,
    private val appPreferencesDatastore: AppPreferencesDatastore,
) {

    private val Context.dummyDataStore: DataStore<UserPreferences> by dataStore(
        fileName = DUMMY_FILENAME,
        serializer = EmptyUserPreferencesSerializer,
    )
    private val dummyDataStore = UserPreferencesDatastore(context.dummyDataStore, "dummy")

    private val _dataStores: MutableList<UserPreferencesDatastore> = mutableListOf()
    val dataStores = _dataStores.toList()

    val activeUserDatastore: Flow<UserPreferencesDatastore> =
        appPreferencesDatastore.activeUserDatastoreFilename.mapLatest { activeDatastoreId ->
            dataStores.find { it.fileName == activeDatastoreId } ?: dummyDataStore
        }

    init {
        val dataStoreFileNames = DatastoreUtils.getAllUserPreferencesDatastoreFilesNames(context)
        dataStoreFileNames.forEach { fileName ->
            val dataStore = dataStoreCreate(
                fileName,
                EmptyUserPreferencesSerializer,
            )
            _dataStores.add(
                UserPreferencesDatastore(
                    dataStore = dataStore,
                    fileName = fileName
                )
            )
        }
    }

    suspend fun createNewUserDatastore(
        domain: String,
        accessToken: String,
        accountId: String,
    ) {
        val fileName = "$domain-$accountId-prefs.pb"
        val dataStore = dataStoreCreate(
            fileName,
            UserPreferencesSerializer(
                domain = domain,
                accessToken = accessToken,
                accountId = accountId,
            )
        )
        val newDataStore = UserPreferencesDatastore(
            dataStore = dataStore,
            fileName = fileName
        ).apply {

        }
        _dataStores.add(
            newDataStore
        )

        appPreferencesDatastore.saveActiveUserDatastoreFilename(fileName)
    }

    private fun dataStoreCreate(
        fileName: String,
        serializer: Serializer<UserPreferences>,
    ): DataStore<UserPreferences> {
        return DataStoreFactory.create(
            storage = OkioStorage(
                FileSystem.SYSTEM,
                OkioSerializerWrapper(serializer)
            ) {
                context.dataStoreFile(fileName).absolutePath.toPath()
            },
            corruptionHandler = null,
            migrations = emptyList(),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
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

private class OkioSerializerWrapper<T>(
    private val serializer: Serializer<T>
) : OkioSerializer<T> {
    override val defaultValue: T
        get() = serializer.defaultValue

    override suspend fun readFrom(source: BufferedSource): T =
        serializer.readFrom(source.inputStream())

    override suspend fun writeTo(t: T, sink: BufferedSink) =
        serializer.writeTo(t, sink.outputStream())
}