package com.satellite.messenger.ui.exit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.satellite.messenger.databinding.FragmentExitBinding
import com.satellite.messenger.di.DaggerAppComponent
import com.satellite.messenger.ui.login.activity.CloseApp

class ExitFragment: Fragment() {

    private lateinit var interface_obmen: CloseApp

    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var exitViewModel: ExitViewModel

    private var _binding: FragmentExitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModelFactory = DaggerAppComponent.create().viewFactory()

        exitViewModel = ViewModelProvider(this, viewModelFactory)[ExitViewModel::class.java]
        _binding = FragmentExitBinding.inflate(inflater, container, false)

        binding.quit.setOnClickListener {
            interface_obmen.closeApp()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CloseApp)
            interface_obmen = context
    }

}