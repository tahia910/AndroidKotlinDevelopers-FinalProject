package com.example.android.nextreminder.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.android.nextreminder.data.SimilarDTO
import com.example.android.nextreminder.databinding.ActivityDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_SIMILAR_ITEM = "extra_similar_item"

        fun startActivity(context: Context, similarItem: SimilarDTO) {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra(EXTRA_SIMILAR_ITEM, similarItem)
            }
            context.startActivity(intent)
        }

        fun newIntent(context: Context, similarItem: SimilarDTO): Intent {
            return Intent(context, DetailActivity::class.java).apply {
                putExtra(EXTRA_SIMILAR_ITEM, similarItem)
            }
        }
    }

    private val viewModel: DetailViewModel by viewModel()
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val similarItem = intent.getParcelableExtra<SimilarDTO>(EXTRA_SIMILAR_ITEM) ?: return
        viewModel.setSimilarItem(similarItem)

        viewModel.similarItem.observe(this) { item ->
            if (item.isBookmarked) {
                binding.detailBookmarkFab.shrink()
            } else {
                binding.detailBookmarkFab.extend()
            }
        }

        viewModel.displayErrorToast.observe(this) { messageStringResource ->
            if (messageStringResource == null) return@observe
            Toast.makeText(this, messageStringResource, Toast.LENGTH_SHORT).show()
            viewModel.toastDisplayed()
        }
    }
}