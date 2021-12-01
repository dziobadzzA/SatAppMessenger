package com.satellite.messenger.ui.feedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.satellite.messenger.R
import com.satellite.messenger.databinding.FragmentFeedbackBinding
import com.satellite.messenger.di.DaggerAppComponent

class FeedbackFragment: Fragment() {

    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var feedbackViewModel: FeedbackViewModel
    private var _binding: FragmentFeedbackBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModelFactory = DaggerAppComponent.create().viewFactory()

        feedbackViewModel = ViewModelProvider(this, viewModelFactory)[FeedbackViewModel::class.java]
        _binding = FragmentFeedbackBinding.inflate(inflater, container, false)


        binding.sendFeedback.setOnClickListener {

            if (feedbackViewModel.sendFeedback(binding.editTextTextEmailAddress.text.toString(), binding.textInputEditText.text.toString())) {
                binding.textInputEditText.setText("")
                binding.textInputEditText.hint = getString(R.string.write_feedback)
            }

        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}