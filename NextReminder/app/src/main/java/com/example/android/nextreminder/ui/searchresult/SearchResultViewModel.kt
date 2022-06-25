package com.example.android.nextreminder.ui.searchresult

import androidx.lifecycle.*
import com.example.android.nextreminder.R
import com.example.android.nextreminder.data.SimilarDTO
import com.example.android.nextreminder.data.SimilarItemTypeEnum
import com.example.android.nextreminder.data.SimilarRepository
import com.example.android.nextreminder.data.isSameAs
import com.example.android.nextreminder.data.local.entityToDtoList
import com.example.android.nextreminder.data.network.Result
import kotlinx.coroutines.launch

class SearchResultViewModel(private val repository: SimilarRepository) : ViewModel() {

    private val _queryString = MutableLiveData<String>()
    val queryString: LiveData<String>
        get() = _queryString

    private val filter = MutableLiveData<SimilarItemTypeEnum>()

    private val _resultList = MutableLiveData<List<SimilarDTO>>()
    val resultList: LiveData<List<SimilarDTO>>
        get() = _resultList

    val bookmarkList: LiveData<List<SimilarDTO>> =
        Transformations.map(repository.getAllBookmarksLiveData()) {
            it.entityToDtoList()
        }

    val loading = MutableLiveData(false)

    fun saveKeywordAndFilter(keyword: String, filter: SimilarItemTypeEnum) {
        _queryString.value = keyword
        this.filter.value = filter
    }

    fun getSimilarMediaList() {
        val keyword = _queryString.value ?: return
        val filter = this.filter.value ?: return

        // If the user changes the orientation, don't query again
        if (_resultList.value != null) return

        loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getSimilarMediaList(keywords = keyword, filter = filter)
            when (result) {
                is Result.Success<*> -> {
                    loading.postValue(false)
                    val data = (result.data as? List<SimilarDTO>) ?: return@launch

                    _resultList.postValue(data)
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
            // The result list will get updated through the bookmark list observer

            if (result is Result.Error) {
                displayBookmarkErrorToast(item.isBookmarked)
            }
        }
    }

    fun displayExtraParsingErrorToast() {
        // Display an error toast and the "Search Again" button to go back
        _displayErrorToast.postValue(R.string.error_extra_parsing)
    }

    private fun displayBookmarkErrorToast(wasOriginallyBookmarked: Boolean) {
        val errorMessage = if (wasOriginallyBookmarked) {
            R.string.error_removing_bookmark
        } else {
            R.string.error_adding_bookmark
        }
        _displayErrorToast.postValue(errorMessage)
    }

    fun searchAgain() {
        _moveToHome.postValue(true)
    }

    private val _moveToHome = MutableLiveData(false)
    val moveToHome: LiveData<Boolean> = _moveToHome
    private val _displayErrorToast = MutableLiveData<Int?>()
    val displayErrorToast: LiveData<Int?> = _displayErrorToast

    fun toastDisplayed() {
        _displayErrorToast.postValue(null)
    }
}
