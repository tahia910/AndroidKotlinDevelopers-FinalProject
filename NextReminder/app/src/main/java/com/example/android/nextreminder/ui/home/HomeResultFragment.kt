package com.example.android.nextreminder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.nextreminder.databinding.FragmentHomeResultBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeResultFragment : Fragment() {

    private val viewModel: HomeViewModel by sharedViewModel()
    private lateinit var binding: FragmentHomeResultBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeResultBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.itemList.adapter = HomeResultAdapter(ItemClickListener { item ->
            // TODO: bookmark
        })

        viewModel.moveToHome.observe(viewLifecycleOwner) {
            if (it == false) return@observe
            findNavController().navigateUp()
            viewModel.moveFinished()
        }
        return binding.root
    }
}