package com.example.hellenicreaderapp.utility

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.hellenicreaderapp.AppState
import kotlinx.coroutines.flow.first

// TODO Save the last text read, the order of reading, the num of texts read

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "StateData")
lateinit var stateDataStore: DataStore<Preferences>
object dataStoreManager {
    fun dataStoreInit(context: Context) {
        stateDataStore = context.dataStore
    }
}

val homeReadOrder = stringPreferencesKey("homeReadOrder")
val homeLastRead = stringPreferencesKey("homeLastRead")
val lastRead = stringPreferencesKey("lastRead")


// Functions and converters
suspend fun saveStateData(key: Preferences.Key<String>, value: String) {
    stateDataStore.edit { preferences ->
        preferences[key] = value
    }
}

suspend fun getStateData(key: Preferences.Key<String>): String {
    val preferences = stateDataStore.data.first()
    Log.d("DataLoad", "Loaded data: ${preferences[key]}")
    return preferences[key] ?: ""
}

object Converters {
    fun fromOrderOfReading(value: AppState.OrderOfReading) = value.name
    fun toOrderOfReading(value: String) = enumValueOf<AppState.OrderOfReading>(value)
}