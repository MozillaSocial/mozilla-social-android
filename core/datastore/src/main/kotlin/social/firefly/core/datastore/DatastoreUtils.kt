package social.firefly.core.datastore

import android.content.Context
import java.io.File

object DatastoreUtils {
    fun getAllUserPreferencesDatastoreFilesNames(
        context: Context,
    ): List<String> {
        val datastoreDir = File(context.filesDir, "datastore")
        val datastoreFiles = if (datastoreDir.exists() && datastoreDir.isDirectory) {
            datastoreDir.listFiles()
        } else {
            null
        }
        return datastoreFiles
            ?.map { it.name }
            ?.filterNot { it == APP_PREFERENCES_DATASTORE_FILENAME }
            ?: emptyList()
    }
}