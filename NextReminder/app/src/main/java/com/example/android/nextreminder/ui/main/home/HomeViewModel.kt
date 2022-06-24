package com.example.android.nextreminder.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _queryString = MutableLiveData<String>()
    val queryString: LiveData<String>
        get() = _queryString

    fun saveKeyword(text: String) {
        _queryString.value = text
    }
}