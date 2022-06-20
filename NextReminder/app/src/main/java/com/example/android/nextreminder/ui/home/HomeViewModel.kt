package com.example.android.nextreminder.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.nextreminder.data.SimilarDTO
import com.example.android.nextreminder.data.SimilarRepository
import com.example.android.nextreminder.data.network.Result
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: SimilarRepository) : ViewModel() {

    private val _queryString = MutableLiveData<String>()
    val queryString: LiveData<String>
        get() = _queryString

    private val _resultList = MutableLiveData<List<SimilarDTO>>()
    val resultList: LiveData<List<SimilarDTO>>
        get() = _resultList

    val loading = MutableLiveData(false)

    fun getSimilarMedia(keywords: String) {
        loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getSimilarMedia(keywords)
            when (result) {
                is Result.Success<*> -> {
                    val data = (result.data as? Pair<String, List<SimilarDTO>>) ?: return@launch
                    _queryString.postValue(data.first)
                    _resultList.postValue(data.second)
                    _moveToResult.postValue(true)
                    loading.postValue(false)
                }
                is Result.Error -> {
                    loading.postValue(false)
                    _displayErrorToast.postValue(true)
                }
            }
        }
    }

    fun addBookmark(item: SimilarDTO) {
        viewModelScope.launch {
            val result = repository.addBookmark(item)
            when (result) {
                is Result.Success<*> -> { /** Display toast for both cases **/ }
                is Result.Error -> {}
            }
        }
    }

    fun removeBookmark(item: SimilarDTO) {
        viewModelScope.launch {
            val result = repository.removeBookmark(item)
            when (result) {
                is Result.Success<*> -> { /** Display toast for both cases **/ }
                is Result.Error -> {}
            }
        }
    }

    // TODO: timing or don't use query string?
    fun searchAgain() {
        _moveToHome.postValue(true)
        _queryString.postValue("")
        _resultList.postValue(null)
    }

    private val _moveToResult = MutableLiveData(false)
    val moveToResult: LiveData<Boolean> = _moveToResult
    private val _moveToHome = MutableLiveData(false)
    val moveToHome: LiveData<Boolean> = _moveToHome
    private val _displayErrorToast = MutableLiveData(false)
    val displayErrorToast: LiveData<Boolean> = _displayErrorToast

    fun moveFinished() {
        _moveToResult.value = false
        _moveToHome.value = false
    }

    fun toastDisplayed() {
        _displayErrorToast.value = false
    }
}