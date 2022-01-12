package app.dealux.orangerestaurant.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import app.dealux.orangerestaurant.constants.OrangeRestaurantConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val preferenceName = OrangeRestaurantConstants.AUTH.TYPE_OF_AUTH_PREFERENCE
private const val preferenceKey = OrangeRestaurantConstants.AUTH.AUTH_PREFERENCE_KEY

class AuthDataStoreRepository(val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(preferenceName)

    suspend fun save(value: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val dataStoreKey = booleanPreferencesKey(preferenceKey)
            context.dataStore.edit { settings ->
                settings[dataStoreKey] = value
            }
        }
    }

    fun read(): Flow<Boolean?> {
        val dataStoreKey = booleanPreferencesKey(preferenceKey)
        return context.dataStore.data.map { preference ->
            preference[dataStoreKey] ?: null
        }
    }

}