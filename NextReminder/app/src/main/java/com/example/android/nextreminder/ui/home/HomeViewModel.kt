package com.example.android.nextreminder.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.nextreminder.data.SimilarList
import com.example.android.nextreminder.data.SimilarRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: SimilarRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    init {
        getSimilarMedia()
    }
    fun getSimilarMedia() {
        viewModelScope.launch {
            val result = repository.getSimilarMedia("android")
            _text.postValue(result.resultList?.getOrNull(0)?.name ?: "nope")
        }
    }
}