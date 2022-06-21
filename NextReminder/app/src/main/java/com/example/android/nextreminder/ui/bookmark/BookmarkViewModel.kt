package com.example.android.nextreminder.ui.bookmark

import androidx.lifecycle.*
import com.example.android.nextreminder.R
import com.example.android.nextreminder.data.SimilarDTO
import com.example.android.nextreminder.data.SimilarRepository
import com.example.android.nextreminder.data.local.entityToDtoList
import com.example.android.nextreminder.data.network.Result
import kotlinx.coroutines.launch

class BookmarkViewModel(private val repository: SimilarRepository) : ViewModel() {

    val bookmarkList: LiveData<List<SimilarDTO>> =
        Transformations.map(repository.getAllBookmarksLiveData()) {
            it.entityToDtoList()
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

    fun snackBarDisplayed() {
        _displayBookmarkDeletedSnackBar.value = null
    }

    fun toastDisplayed() {
        _displayErrorToast.value = null
    }
}