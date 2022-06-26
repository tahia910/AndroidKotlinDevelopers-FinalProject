package com.example.android.nextreminder.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.nextreminder.R
import com.example.android.nextreminder.data.SimilarDTO
import com.example.android.nextreminder.data.SimilarRepository
import com.example.android.nextreminder.data.network.Result
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: SimilarRepository) : ViewModel() {

    private val _similarItem = MutableLiveData<SimilarDTO>()
    val similarItem: LiveData<SimilarDTO>
        get() = _similarItem

    fun setSimilarItem(similarItem: SimilarDTO) {
        // If the user had changed the bookmark status before a configuration change, this
        // would set back the original status, so we keep the latest state
        if (this._similarItem.value != null) return
        this._similarItem.value = similarItem
    }

    val loading = MutableLiveData(false)

    fun addOrRemoveBookmark() {
        _similarItem.value?.let { item ->
            if (item.isBookmarked) removeBookmark(item) else addBookmark(item)
        }
    }

    private fun removeBookmark(item: SimilarDTO) {
        loading.postValue(true)
        viewModelScope.launch {
            val result = repository.removeBookmark(item)
            when (result) {
                is Result.Success<*> -> {
                    val new = _similarItem.value?.copy(isBookmarked = false)
                    _similarItem.value = new
                    loading.postValue(false)
                }
                is Result.Error -> {
                    loading.postValue(false)
                    _displayErrorToast.postValue(R.string.error_removing_bookmark)
                }
            }
        }
    }

    private fun addBookmark(item: SimilarDTO) {
        loading.postValue(true)
        viewModelScope.launch {
            val result = repository.addBookmark(item)

            when (result) {
                is Result.Success<*> -> {
                    val new = _similarItem.value?.copy(isBookmarked = true)
                    _similarItem.value = new
                    loading.postValue(false)
                }
                is Result.Error -> {
                    loading.postValue(false)
                    _displayErrorToast.postValue(R.string.error_adding_bookmark)
                }
            }
        }
    }

    fun closeDetail() {
        _moveBack.postValue(true)
    }

    private val _displayErrorToast = MutableLiveData<Int?>()
    val displayErrorToast: LiveData<Int?> = _displayErrorToast
    private val _moveBack = MutableLiveData(false)
    val moveBack: LiveData<Boolean> = _moveBack

    fun toastDisplayed() {
        _displayErrorToast.value = null
    }
}