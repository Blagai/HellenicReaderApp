package com.blagai.hellenicreaderapp.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blagai.hellenicreaderapp.AppState
import com.blagai.hellenicreaderapp.utility.TranslatedTitleMap

class DashboardViewModel : ViewModel() {

    // Change this to be the title of the last text read instead of the file name
    private val _lastReadText = MutableLiveData<String>().apply {
        value = TranslatedTitleMap.mappedTitles[AppState.lastRead]
    }
    val lastReadText: LiveData<String> = _lastReadText

    fun refreshLastRead() {
        _lastReadText.value = TranslatedTitleMap.mappedTitles[AppState.lastRead]
    }
}