package com.example.android.nextreminder.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.nextreminder.R
import com.example.android.nextreminder.data.SimilarDTO
import com.example.android.nextreminder.data.SimilarRepository
import com.example.android.nextreminder.data.isSameAs
import com.example.android.nextreminder.data.network.Result
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: SimilarRepository) : ViewModel() {

    private val _queryString = MutableLiveData<String>()
    val queryString: LiveData<String>
        get() = _queryString

    private val _resultList = MutableLiveData<List<SimilarDTO>>()
    val resultList: LiveData<List<SimilarDTO>>
        get() = _resultList

//    val bookmarkList: LiveData<List<SimilarDTO>> =
//        Transformations.map(repository.getAllBookmarksLiveData()) {
//            it.entityToDtoList()
//        }

    val notifyAdapter = MutableLiveData(false)

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
                    _displayErrorToast.postValue(R.string.error_network)
                }
            }
        }
    }

    private fun updateResultList(item: SimilarDTO) {
        _resultList.value?.map {
            if (it.isSameAs(item)) it.isBookmarked = !it.isBookmarked
        }
        notifyAdapter.postValue(true)
    }

    fun adapterNotified() {
        notifyAdapter.postValue(false)
    }

    fun addOrRemoveBookmark(item: SimilarDTO) {
        viewModelScope.launch {
            val result = if (item.isBookmarked) {
                repository.removeBookmark(item)
            } else {
                repository.addBookmark(item)
            }

            when (result) {
                is Result.Success<*> -> {
                    // TODO: Display SnackBar for both cases + suggest to move to bookmark list?
                    updateResultList(item)
                }
                is Result.Error -> {
                    displayErrorToast(item.isBookmarked)
                }
            }
        }
    }

    private fun displayErrorToast(wasOriginallyBookmarked: Boolean) {
        val errorMessage = if (wasOriginallyBookmarked) {
            R.string.error_removing_bookmark
        } else {
            R.string.error_adding_bookmark
        }
        _displayErrorToast.postValue(errorMessage)
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
    private val _displayErrorToast = MutableLiveData<Int?>()
    val displayErrorToast: LiveData<Int?> = _displayErrorToast

    fun moveFinished() {
        _moveToResult.value = false
        _moveToHome.value = false
    }

    fun toastDisplayed() {
        _displayErrorToast.postValue(null)
    }
}