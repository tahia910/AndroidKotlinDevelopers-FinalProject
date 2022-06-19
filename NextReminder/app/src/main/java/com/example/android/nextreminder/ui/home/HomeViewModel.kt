package com.example.android.nextreminder.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.nextreminder.data.SimilarDTO
import com.example.android.nextreminder.data.SimilarRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: SimilarRepository) : ViewModel() {

    private val _resultList = MutableLiveData<List<SimilarDTO>>()
    val resultList: LiveData<List<SimilarDTO>>
        get() = _resultList

    fun getSimilarMedia(keywords: String) {
        viewModelScope.launch {
            val result = repository.getSimilarMedia(keywords)
            _resultList.postValue(result)
            _moveToResult.postValue(true)
        }
    }

    private val _moveToResult = MutableLiveData(false)
    val moveToResult: LiveData<Boolean> = _moveToResult
    private val _moveToHome = MutableLiveData(false)
    val moveToHome: LiveData<Boolean> = _moveToHome

    fun moveFinished() {
        _moveToResult.value = false
        _moveToHome.value = false
    }
}