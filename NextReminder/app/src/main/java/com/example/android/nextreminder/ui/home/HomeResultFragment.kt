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

        viewModel.notifyAdapter.observe(viewLifecycleOwner) {
            if (!it) return@observe
            adapter.notifyDataSetChanged()
            viewModel.adapterNotified()
        }

        viewModel.moveToHome.observe(viewLifecycleOwner) {
            if (!it) return@observe
            findNavController().navigateUp()
            viewModel.moveFinished()
        }

        viewModel.displayErrorToast.observe(viewLifecycleOwner) { messageStringResource ->
            if (messageStringResource == null) return@observe
            Toast.makeText(requireContext(), messageStringResource, Toast.LENGTH_SHORT).show()
            viewModel.toastDisplayed()
        }

        return binding.root
    }
}