package com.example.multimediachallenge.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.multimediachallenge.databinding.FragmentVideoBinding


class VideoFragment : Fragment() {

    private lateinit var binding: FragmentVideoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }
}