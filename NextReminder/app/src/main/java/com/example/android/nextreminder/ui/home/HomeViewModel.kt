package com.example.android.nextreminder.ui.home

import androidx.lifecycle.*
import com.example.android.nextreminder.R
import com.example.android.nextreminder.data.SimilarDTO
import com.example.android.nextreminder.data.SimilarRepository
import com.example.android.nextreminder.data.isSameAs
import com.example.android.nextreminder.data.local.entityToDtoList
import com.example.android.nextreminder.data.network.Result
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: SimilarRepository) : ViewModel() {

    private val _queryString = MutableLiveData<String>()
    val queryString: LiveData<String>
        get() = _queryString

    private val _resultList = MutableLiveData<List<SimilarDTO>>()
    val resultList: LiveData<List<SimilarDTO>>
        get() = _resultList

    val bookmarkList: LiveData<List<SimilarDTO>> =
        Transformations.map(repository.getAllBookmarksLiveData()) {
            it.entityToDtoList()
        }

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

    /**
     * This method only gets called when one item's status has been changed manually,
     * or if the list was modified in the bookmark list screen.
     * (The original result list was updated already after calling the API in the repository)
     */
    fun updateResultListBookmarks(bookmarkList: List<SimilarDTO>): Boolean {
        var hasUpdatedList = false
        _resultList.value?.map { item ->
            // If the item does not exist in the bookmark list, then the value should be false
            val isBookmarked = bookmarkList.find { it.isSameAs(item) }?.isBookmarked ?: false

            // The item bookmark status is not correct
            if (item.isBookmarked != isBookmarked) {
                hasUpdatedList = true
                // The current status could be false or true, just make sure it becomes its contrary
                item.isBookmarked = !item.isBookmarked
            }
        }
        return hasUpdatedList
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