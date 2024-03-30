package com.example.multimediachallenge.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.multimediachallenge.databinding.FragmentVideoBinding


class VideoFragment : Fragment() {

    private lateinit var mediaController: MediaController
    private lateinit var binding: FragmentVideoBinding
    private val contractGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                binding.video.setVideoURI(uri)
                mediaController = MediaController(requireContext())
                mediaController.setMediaPlayer(binding.video)
                binding.video.setMediaController(mediaController)
                binding.video.start()

            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFindVideo.setOnClickListener {
            contractGallery.launch("video/*")
        }
    }
}