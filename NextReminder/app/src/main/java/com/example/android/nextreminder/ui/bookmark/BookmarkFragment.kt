package com.example.android.nextreminder.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.nextreminder.R
import com.example.android.nextreminder.databinding.FragmentBookmarkBinding
import com.example.android.nextreminder.ui.home.HomeResultAdapter
import com.example.android.nextreminder.ui.home.ItemClickListener
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
        binding.bookmarkList.adapter = HomeResultAdapter(ItemClickListener { item ->
            viewModel.removeBookmark(item)
        })

        viewModel.displayBookmarkDeletedSnackBar.observe(viewLifecycleOwner) { deletedItem ->
            if (deletedItem == null) return@observe
            // TODO: change text
            Snackbar.make(binding.root, "Add again?", Snackbar.LENGTH_SHORT)
                .setAction("YES") {
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

        binding.goSearchButton.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }
}