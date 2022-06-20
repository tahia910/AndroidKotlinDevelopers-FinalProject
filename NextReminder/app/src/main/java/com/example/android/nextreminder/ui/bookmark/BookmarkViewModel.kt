package com.example.android.nextreminder.ui.bookmark

import androidx.lifecycle.*
import com.example.android.nextreminder.data.SimilarDTO
import com.example.android.nextreminder.data.SimilarRepository
import com.example.android.nextreminder.data.local.entityToDtoList
import kotlinx.coroutines.launch
import com.example.android.nextreminder.data.network.Result

class BookmarkViewModel(private val repository: SimilarRepository) : ViewModel() {

    private val loading = MutableLiveData(false)

//    private val _bookmarkList = MutableLiveData<List<SimilarDTO>>()
//    val bookmarkList: LiveData<List<SimilarDTO>>
//        get() = _bookmarkList
//
//    init {
//        getAllBookmarks()
//    }
//
//    private fun getAllBookmarks() {
//        loading.postValue(true)
//        viewModelScope.launch {
//            val result = repository.getAllBookmarks()
//            when (result) {
//                is Result.Success<*> -> {
//                    val data = (result.data as? List<SimilarDTO>) ?: return@launch
//                    _bookmarkList.postValue(data)
//                    loading.postValue(false)
//                }
//                is Result.Error -> {
//                    loading.postValue(false)
//                    // TODO: display error
//                }
//            }
//        }
//    }

    val bookmarkList: LiveData<List<SimilarDTO>> = Transformations.map(repository.getAllBookmarks()) {
        it.entityToDtoList()
    }

    fun removeBookmark(item: SimilarDTO) {
        viewModelScope.launch {
            val result = repository.removeBookmark(item)
            when (result) {
                is Result.Success<*> -> {
                    _displayBookmarkDeletedSnackBar.postValue(item)
                }
                is Result.Error -> {}
            }
        }
    }

    fun addBackBookmark(item: SimilarDTO) {
        viewModelScope.launch {
            val result = repository.addBookmark(item)
            when (result) {
                is Result.Success<*> -> {
                    // TODO: Display confirmation toast
                }
                is Result.Error -> {}
            }
        }
    }

    private val _displayBookmarkDeletedSnackBar = MutableLiveData<SimilarDTO?>()
    val displayBookmarkDeletedSnackBar: LiveData<SimilarDTO?> = _displayBookmarkDeletedSnackBar

    fun snackBarDisplayed() {
        _displayBookmarkDeletedSnackBar.value = null
    }
}