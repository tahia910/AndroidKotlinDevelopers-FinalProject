package com.example.android.nextreminder.ui.main

import androidx.lifecycle.*
import com.example.android.nextreminder.R
import com.example.android.nextreminder.data.SimilarDTO
import com.example.android.nextreminder.data.SimilarRepository
import com.example.android.nextreminder.data.local.entityToDtoList
import com.example.android.nextreminder.data.network.Result
import kotlinx.coroutines.launch

class MainViewModel(private val repository: SimilarRepository) : ViewModel() {

    val bookmarkList: LiveData<List<SimilarDTO>> =
        Transformations.map(repository.getAllBookmarksLiveData()) {
            it.entityToDtoList()
        }

    private val _queryString = MutableLiveData<String>()
    val queryString: LiveData<String>
        get() = _queryString

    fun saveKeyword(text: String) {
        _queryString.value = text
    }

    fun getRandomBookmark() {
        if (bookmarkList.value.isNullOrEmpty()) {
            _displayErrorToast.postValue(R.string.error_no_bookmarks)
            return
        }
        val randomBookmark = bookmarkList.value?.random() ?: return
        _moveToDetail.postValue(randomBookmark)
    }

    fun removeBookmark(item: SimilarDTO) {
        viewModelScope.launch {
            val result = repository.removeBookmark(item)
            when (result) {
                is Result.Success<*> -> {
                    _displayBookmarkDeletedSnackBar.postValue(item)
                }
                is Result.Error -> {
                    _displayErrorToast.postValue(R.string.error_removing_bookmark)
                }
            }
        }
    }

    fun addBackBookmark(item: SimilarDTO) {
        viewModelScope.launch {
            val result = repository.addBookmark(item)
            if (result is Result.Error) {
                _displayErrorToast.postValue(R.string.error_adding_bookmark)
            }
        }
    }

    private val _displayBookmarkDeletedSnackBar = MutableLiveData<SimilarDTO?>()
    val displayBookmarkDeletedSnackBar: LiveData<SimilarDTO?> = _displayBookmarkDeletedSnackBar
    private val _displayErrorToast = MutableLiveData<Int?>()
    val displayErrorToast: LiveData<Int?> = _displayErrorToast
    private val _moveToDetail = MutableLiveData<SimilarDTO?>()
    val moveToDetail: LiveData<SimilarDTO?> = _moveToDetail

    fun snackBarDisplayed() {
        _displayBookmarkDeletedSnackBar.value = null
    }

    fun toastDisplayed() {
        _displayErrorToast.value = null
    }

    fun moveFinished() {
        _moveToDetail.value = null
    }
}