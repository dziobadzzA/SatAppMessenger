package com.satellite.messenger.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.satellite.messenger.databinding.FragmentProfileBinding
import com.satellite.messenger.di.DaggerAppComponent
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION

class ProfileFragment: Fragment() {

    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var profileViewModel: ProfileViewModel

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var interface_obmen:GetProfile

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModelFactory = DaggerAppComponent.create().viewFactory()

        profileViewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        activity?.let {
            profileViewModel.repository.setApp(it.application)
            profileViewModel.getProfile()
        }

        binding.progressBarUpload.progress = 0
        binding.progressBarUpload.isVisible = false

        binding.imageViewProfile.setOnClickListener {

            val permissionStatus = activity?.let { it1 -> ContextCompat.checkSelfPermission(it1, Manifest.permission.WRITE_EXTERNAL_STORAGE) }

            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                activity?.let { it1 -> ActivityCompat.requestPermissions(it1, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0) }
            }

            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                intent.flags = FLAG_GRANT_WRITE_URI_PERMISSION or FLAG_GRANT_READ_URI_PERMISSION
                startForResult.launch(intent)
            }

        }

        profileViewModel.bmp?.observe(viewLifecycleOwner,  {
            binding.imageViewProfile.setImageBitmap(it?.let { it1 -> Bitmap.createScaledBitmap(it1, binding.imageViewProfile.width, binding.imageViewProfile.height, false)
            })
            if (profileViewModel.sendToInterface)
                interface_obmen.changeImage()
        })

        profileViewModel.progress?.observe(viewLifecycleOwner,  {

            binding.progressBarUpload.isVisible =
                !((binding.progressBarUpload.progress <= 0) or (binding.progressBarUpload.progress >= 99.9))

            binding.progressBarUpload.progress = it
        })

        profileViewModel.email = interface_obmen.getAuth().toString()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            val imageStream = imageUri?.let { context?.contentResolver?.openInputStream(it) }
            val selectedImage = BitmapFactory.decodeStream(imageStream)
            binding.imageViewProfile.setImageBitmap(selectedImage)
            imageUri?.let {
                this.context?.let { it1 -> profileViewModel.getPath(it, it1) }
                profileViewModel.push(it)
                interface_obmen.changeImage()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GetProfile)
            interface_obmen = context
    }



}