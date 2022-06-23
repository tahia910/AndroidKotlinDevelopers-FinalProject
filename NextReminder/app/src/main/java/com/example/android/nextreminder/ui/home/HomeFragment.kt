package com.example.android.nextreminder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.nextreminder.R
import com.example.android.nextreminder.data.SimilarItemTypeEnum
import com.example.android.nextreminder.databinding.FragmentHomeBinding
import com.google.android.material.chip.Chip
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
            val keyword =
                binding.homeTextField.editText?.text?.toString() ?: return@setOnClickListener
            if (!isValidSearchKeyword(keyword)) return@setOnClickListener

            val filter = getCheckedFilter()
            viewModel.getSimilarMediaList(keywords = keyword, filter = filter)
        }

        binding.homeTextField.editText?.doOnTextChanged { text, _, _, _ ->
            if (isValidSearchKeyword(text)) viewModel.saveKeyword(text.toString())
        }

        setObservers()

        return binding.root
    }

    // https://betterprogramming.pub/textinputlayout-form-validation-using-data-binding-in-android-86aea8645a11
    private fun isValidSearchKeyword(text: CharSequence?): Boolean {
        return if (text?.trim(' ', ',').isNullOrBlank()) {
            binding.homeTextField.error = getString(R.string.error_empty_keyword)
            binding.homeTextField.requestFocus()
            false
        } else {
            binding.homeTextField.isErrorEnabled = false
            viewModel.saveKeyword(text.toString())
            true
        }
    }

    private fun getCheckedFilter(): SimilarItemTypeEnum {
        val chip = binding.homeChipGroup.children.first { (it as Chip).isChecked } as Chip
        return SimilarItemTypeEnum.getEnumFromLabel(chip.text.toString())
    }

    private fun setObservers() {
        viewModel.moveToResult.observe(viewLifecycleOwner) {
            if (it == false) return@observe
            findNavController().navigate(
                HomeFragmentDirections.actionNavigationHomeToNavigationHomeResult()
            )
            viewModel.moveFinished()
        }

        viewModel.displayErrorToast.observe(viewLifecycleOwner) { messageStringResource ->
            if (messageStringResource == null) return@observe
            Toast.makeText(requireContext(), messageStringResource, Toast.LENGTH_SHORT).show()
            viewModel.toastDisplayed()
        }
    }
}