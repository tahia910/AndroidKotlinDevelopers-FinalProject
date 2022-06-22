package com.example.android.nextreminder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.nextreminder.databinding.FragmentHomeResultBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeResultFragment : Fragment() {

    private val viewModel: HomeViewModel by sharedViewModel()
    private lateinit var binding: FragmentHomeResultBinding
    private lateinit var adapter: HomeResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeResultBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        adapter = HomeResultAdapter(ItemClickListener { item ->
            viewModel.addOrRemoveBookmark(item)
        })
        binding.itemList.adapter = adapter

        viewModel.moveToHome.observe(viewLifecycleOwner) {
            if (!it) return@observe
            findNavController().navigateUp()
            viewModel.moveFinished()
        }

        viewModel.bookmarkList.observe(viewLifecycleOwner) { bookmarkList ->
            if (bookmarkList.isNullOrEmpty()) return@observe
            // To avoid calling the adapter notifier unnecessarily, check if we changed anything
            val hasUpdatedList = viewModel.updateResultListBookmarks(bookmarkList)
            if (hasUpdatedList) adapter.notifyDataSetChanged()
        }

        viewModel.displayErrorToast.observe(viewLifecycleOwner) { messageStringResource ->
            if (messageStringResource == null) return@observe
            Toast.makeText(requireContext(), messageStringResource, Toast.LENGTH_SHORT).show()
            viewModel.toastDisplayed()
        }

        return binding.root
    }
}