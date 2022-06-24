package com.example.android.nextreminder.ui.main.bookmark

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
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookmarkFragment : Fragment() {

    private val viewModel: BookmarkViewModel by viewModel()
    private lateinit var binding: FragmentBookmarkBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.bookmarkList.adapter = SimilarListAdapter(ItemClickListener { item ->
            viewModel.removeBookmark(item)
        })

        setObservers()

        binding.goSearchButton.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    private fun setObservers() {
        viewModel.displayBookmarkDeletedSnackBar.observe(viewLifecycleOwner) { deletedItem ->
            if (deletedItem == null) return@observe

            val message = getString(R.string.home_result_snackbar_title, deletedItem.name)
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
                .setAction(R.string.home_result_snackbar_action) {
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