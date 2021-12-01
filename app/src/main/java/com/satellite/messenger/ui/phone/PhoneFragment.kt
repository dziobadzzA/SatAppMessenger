package com.satellite.messenger.ui.phone

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.satellite.messenger.databinding.FragmentPhoneBinding
import com.satellite.messenger.di.DaggerAppComponent
import com.satellite.messenger.ui.phone.adapter.PhoneAdapter
import com.satellite.messenger.ui.phone.adapter.PhoneListener
import com.satellite.messenger.utils.state.PhoneModel

class PhoneFragment: Fragment(), PhoneListener {

    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var phonesViewModel: PhonesViewModel
    private var _binding: FragmentPhoneBinding? = null
    private val binding get() = _binding!!

    private var adapter:PhoneAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModelFactory = DaggerAppComponent.create().viewFactory()

       phonesViewModel = ViewModelProvider(this, viewModelFactory)[PhonesViewModel::class.java]
        _binding = FragmentPhoneBinding.inflate(inflater, container, false)

        adapter = PhoneAdapter(this)
        binding.recyclerPhone.adapter = adapter

        activity?.let {

            if (phonesViewModel.itemPhone?.value.isNullOrEmpty())
                phonesViewModel.getListPhone()

            phonesViewModel.itemPhone?.observe(viewLifecycleOwner, {
                adapter!!.submitList(it)
            })
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun call(phone: PhoneModel) {

        if (phonesViewModel.controlCalling(activity?.packageManager!!)) {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:" + phone.phone)
            startActivity(intent)
        }

    }

}