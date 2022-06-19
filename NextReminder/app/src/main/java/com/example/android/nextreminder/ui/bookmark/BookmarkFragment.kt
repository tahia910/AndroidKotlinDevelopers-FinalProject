package com.example.android.nextreminder.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.nextreminder.databinding.FragmentBookmarkBinding

class BookmarkFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bookmarkViewModel = ViewModelProvider(this)[BookmarkViewModel::class.java]

        val binding = FragmentBookmarkBinding.inflate(inflater, container, false)

        return binding.root
    }
}