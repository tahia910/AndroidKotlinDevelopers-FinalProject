package com.example.android.nextreminder.ui.searchresult

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.android.nextreminder.data.SimilarItemTypeEnum
import com.example.android.nextreminder.databinding.ActivitySearchResultBinding
import com.example.android.nextreminder.ui.ItemClickListener
import com.example.android.nextreminder.ui.SimilarListAdapter
import com.example.android.nextreminder.ui.detail.DetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchResultActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_KEYWORD = "extra_keyword"
        private const val EXTRA_FILTER = "extra_filter"

        fun startActivity(context: Context, keyword: String, filter: SimilarItemTypeEnum) {
            val intent = Intent(context, SearchResultActivity::class.java).apply {
                putExtra(EXTRA_KEYWORD, keyword)
                putExtra(EXTRA_FILTER, filter)
            }
            context.startActivity(intent)
        }
    }

    private val viewModel: SearchResultViewModel by viewModel()
    private lateinit var binding: ActivitySearchResultBinding
    private lateinit var adapter: SimilarListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = SimilarListAdapter(
            ItemClickListener(
                bookmarkClickListener = { item -> viewModel.addOrRemoveBookmark(item) },
                itemClickListener = { item ->
                    DetailActivity.startActivity(this, item)
                }
            )
        )
        binding.itemList.adapter = adapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setObservers()

        val keyword = intent.getStringExtra(EXTRA_KEYWORD)
        val filter = intent.getSerializableExtra(EXTRA_FILTER) as? SimilarItemTypeEnum
        if (keyword == null || filter == null) {
            viewModel.displayExtraParsingErrorToast()
        } else {
            viewModel.saveKeywordAndFilter(keyword, filter)
        }
    }

    private fun setObservers() {
        viewModel.queryString.observe(this) {
            if (it != null) viewModel.getSimilarMediaList()
        }

        viewModel.moveToHome.observe(this) {
            if (it == true) finish()
        }

        viewModel.bookmarkList.observe(this) { bookmarkList ->
            if (bookmarkList.isNullOrEmpty()) return@observe
            // To avoid calling the adapter notifier unnecessarily, check if we changed anything
            val hasUpdatedList = viewModel.updateResultListBookmarks(bookmarkList)
            if (hasUpdatedList) adapter.notifyDataSetChanged()
        }

        viewModel.displayErrorToast.observe(this) { messageStringResource ->
            if (messageStringResource == null) return@observe
            Toast.makeText(this, messageStringResource, Toast.LENGTH_SHORT).show()
            viewModel.toastDisplayed()
        }
    }
}