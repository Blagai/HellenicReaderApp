package com.example.hellenicreaderapp.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hellenicreaderapp.AppState

class DashboardViewModel : ViewModel() {

    // Change this to be the title of the last text read instead of the file name
    private val _lastReadText = MutableLiveData<String>().apply {
        value = AppState.lastRead
    }
    val lastReadText: LiveData<String> = _lastReadText

    fun refreshLastRead() {
        _lastReadText.value = AppState.lastRead
    }
}