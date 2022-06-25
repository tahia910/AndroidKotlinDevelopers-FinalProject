package com.example.android.nextreminder.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.android.nextreminder.R
import com.example.android.nextreminder.data.SimilarItemTypeEnum
import com.example.android.nextreminder.databinding.FragmentHomeBinding
import com.example.android.nextreminder.ui.searchresult.SearchResultActivity
import com.google.android.material.chip.Chip
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment() {

    private val viewModel: MainViewModel by sharedViewModel()
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

            val intent = SearchResultActivity.newIntent(
                context = requireContext(),
                keyword = keyword,
                filter = filter
            )
            startActivity(intent)
        }

        binding.homeTextField.editText?.doOnTextChanged { text, _, _, _ ->
            isValidSearchKeyword(text)
            viewModel.saveKeyword(text.toString())
        }

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
}