package com.example.hellenicreaderapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hellenicreaderapp.AppState

class HomeViewModel : ViewModel() {

    private val _readTextsNum = MutableLiveData<String>().apply {
        value = AppState.readTextsNum.toString()
    }
    val readTextsNum: LiveData<String> = _readTextsNum

    private val _buttonVisibility = MutableLiveData<Boolean>().apply {
        value = AppState.homeReadingOrder != AppState.OrderOfReading.NULL
    }
    val buttonVisibility: LiveData<Boolean> = _buttonVisibility
}