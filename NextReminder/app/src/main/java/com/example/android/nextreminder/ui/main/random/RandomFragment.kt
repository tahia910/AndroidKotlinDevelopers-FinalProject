package com.example.android.nextreminder.ui.main.random

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android.nextreminder.databinding.FragmentRandomBinding

class RandomFragment : Fragment() {

    private lateinit var binding: FragmentRandomBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRandomBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        // TODO: add shaking logic (accelerometer) & random DB search
        return binding.root
    }
}