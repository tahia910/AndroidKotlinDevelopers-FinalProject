package com.example.android.nextreminder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.nextreminder.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by sharedViewModel()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.homeSearchButton.setOnClickListener {
            val keywords = binding.homeTextField.editText?.text?.toString()
            if (keywords.isNullOrBlank()) return@setOnClickListener
            viewModel.getSimilarMedia(keywords)
        }

        viewModel.moveToResult.observe(viewLifecycleOwner) {
            if (it == false) return@observe
            findNavController().navigate(
                HomeFragmentDirections.actionNavigationHomeToNavigationHomeResult()
            )
            viewModel.moveFinished()
        }
        return binding.root
    }
}