package com.example.multimediachallenge.ui

import android.Manifest
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.multimediachallenge.databinding.FragmentRecorderBinding

class RecorderFragment : Fragment() {

    private lateinit var binding: FragmentRecorderBinding
    private lateinit var recorder: MediaRecorder
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecorderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkRecorderPermission()
    }

    private fun checkRecorderPermission() {
        val requestRecorderPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    // TODO: aquí hay que hacer las cositas
                    Toast.makeText(requireContext(), "Permisos aceptado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Permisos denegados por el usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestRecorderPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        } else {
            // TODO: aquí hay que hacer las cositas
        }

    }
}