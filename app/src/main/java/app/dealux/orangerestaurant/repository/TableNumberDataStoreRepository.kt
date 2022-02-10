package app.dealux.orangerestaurant.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.dealux.orangerestaurant.OrangeRestaurantConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Singleton

private const val preferenceName = OrangeRestaurantConstants.TABLE.TABLE_NUMBER_PREFERENCE
private const val preferenceKey = OrangeRestaurantConstants.TABLE.TABLE_NUMBER_KEY

class TableNumberDataStoreRepository(val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(preferenceName)
    }

    @Singleton
    suspend fun save(value: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val dataStoreKey = stringPreferencesKey(preferenceKey)
            context.dataStore.edit { settings ->
                settings[dataStoreKey] = value
            }
        }
    }

    @Singleton
    fun read(): Flow<String?> {
        val dataStoreKey = stringPreferencesKey(preferenceKey)
        return context.dataStore.data.map { preference ->
            preference[dataStoreKey] ?: null
        }
    }

    suspend fun delete() {
        val dataStoreKey = stringPreferencesKey(preferenceKey)
        context.dataStore.edit {
            it.remove(dataStoreKey)
        }
    }


}