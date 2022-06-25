package com.example.android.nextreminder.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.nextreminder.R
import com.example.android.nextreminder.databinding.FragmentBookmarkBinding
import com.example.android.nextreminder.ui.ItemClickListener
import com.example.android.nextreminder.ui.SimilarListAdapter
import com.example.android.nextreminder.ui.detail.DetailActivity
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BookmarkFragment : Fragment() {

    private val viewModel: MainViewModel by sharedViewModel()
    private lateinit var binding: FragmentBookmarkBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.bookmarkList.adapter = SimilarListAdapter(
            ItemClickListener(
                bookmarkClickListener = { item -> viewModel.removeBookmark(item) },
                itemClickListener = { item ->
                    val intent = DetailActivity.newIntent(requireContext(), item)
                    startActivity(intent)
                }
            )
        )

        setObservers()

        binding.goSearchButton.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    private fun setObservers() {
        viewModel.displayBookmarkDeletedSnackBar.observe(viewLifecycleOwner) { deletedItem ->
            if (deletedItem == null) return@observe

            val message = getString(R.string.bookmarks_snackbar_title, deletedItem.name)

            val anchorView = (requireActivity() as MainActivity).getBottomNavView()
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
                .setAnchorView(anchorView)
                .setAction(R.string.bookmarks_snackbar_action) {
                    viewModel.addBackBookmark(deletedItem)
                }
                .show()
            viewModel.snackBarDisplayed()
        }

        viewModel.displayErrorToast.observe(viewLifecycleOwner) { messageStringResource ->
            if (messageStringResource == null) return@observe
            Toast.makeText(requireContext(), messageStringResource, Toast.LENGTH_SHORT).show()
            viewModel.toastDisplayed()
        }
    }
}