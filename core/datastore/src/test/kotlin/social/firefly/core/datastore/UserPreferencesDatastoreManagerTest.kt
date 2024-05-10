package social.firefly.core.datastore

import android.content.Context
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test

class UserPreferencesDatastoreManagerTest {
    private lateinit var subject: UserPreferencesDatastoreManager

    private val context: Context = mockk(relaxed = true)
    private val appPreferencesDatastore: AppPreferencesDatastore = mockk(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        subject = UserPreferencesDatastoreManager(
            context = context,
            appPreferencesDatastore = appPreferencesDatastore,
        )
    }
    
    @Test
    fun ensureFileFormatIsCorrect() {
        
    }
}