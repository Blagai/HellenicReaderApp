package com.example.hellenicreaderapp.utility

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.hellenicreaderapp.AppState

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "StateData")

val homeReadOrder = stringPreferencesKey("homeReadOrder")


// Functions and converters
fun saveStateData(context: Context, key: Preferences.Key<String>, value: String) {
    context.dataStore
}

object Converters {
    fun fromOrderOfReading(value: AppState.OrderOfReading) = value.name
    fun toOrderOfReading(value: String) = enumValueOf<AppState.OrderOfReading>(value)
}

