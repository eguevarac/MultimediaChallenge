package com.example.multimediachallenge.ui.fragments

import android.Manifest
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.example.multimediachallenge.databinding.FragmentImgBinding

class ImgFragment : Fragment() {

    private lateinit var binding: FragmentImgBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImgBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFindImg.setOnClickListener {
            checkReadMediaImagesPermission()
        }
    }

    //Permissions -----------------------------------------------------------------
    private fun checkReadMediaImagesPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestReadMediaImagesPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                requestReadMediaImagesPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            contractGallery.launch("image/*")
        }
    }

    private val requestReadMediaImagesPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                contractGallery.launch("image/*")
            } else {
                Toast.makeText(requireContext(), "No has aceptado los permisos", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    //Contracts -----------------------------------------------------------------
    private val contractGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                contractCropImage.launch(
                    CropImageContractOptions(
                        uri, CropImageOptions(
                            guidelines = CropImageView.Guidelines.ON,
                            outputCompressFormat = Bitmap.CompressFormat.PNG
                        )
                    )
                )
            }
        }

    private val contractCropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            binding.img.setImageURI(result.uriContent)
        }
    }
}