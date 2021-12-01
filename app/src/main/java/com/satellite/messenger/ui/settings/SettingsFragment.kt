package com.satellite.messenger.ui.settings

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.satellite.messenger.R
import com.satellite.messenger.databinding.FragmentSettingsBinding
import com.satellite.messenger.di.DaggerAppComponent

class SettingsFragment: Fragment() {

    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModelFactory = DaggerAppComponent.create().viewFactory()

        settingsViewModel = ViewModelProvider(this, viewModelFactory)[SettingsViewModel::class.java]
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        settingsViewModel.text.observe(viewLifecycleOwner,  {
             binding.textSettings.text = it
        })

        activity?.let {
            settingsViewModel.repository.setApp(it.application)
        }

        binding.keyOpen.setOnClickListener {

            val permissionStatus = activity?.let { it1 -> ContextCompat.checkSelfPermission(it1, Manifest.permission.READ_EXTERNAL_STORAGE) }
            if (permissionStatus != PackageManager.PERMISSION_GRANTED)
                activity?.let { it1 -> ActivityCompat.requestPermissions(it1, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0) }
            
            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "text/*"
                startForResult.launch(intent)
            }

        }

        settingsViewModel.getKey()

        settingsViewModel.text.observe(viewLifecycleOwner, {
            binding.textSettings.text = it
        })

        if (savedInstanceState == null) {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, PreferenceScreen())
                .commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { context?.let { it1 -> settingsViewModel.readKey(it, it1) } }
        }
    }

}