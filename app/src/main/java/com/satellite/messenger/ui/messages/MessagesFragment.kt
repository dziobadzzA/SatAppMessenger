package com.satellite.messenger.ui.messages

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.satellite.messenger.databinding.FragmentMessagesBinding
import com.satellite.messenger.di.DaggerAppComponent
import com.satellite.messenger.ui.messages.adapter.MessageAdapter
import com.satellite.messenger.ui.messages.service.MessageService
import com.satellite.messenger.ui.profile.GetProfile
import com.satellite.messenger.utils.settings

class MessagesFragment: Fragment() {


    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var messagesViewModel: MessagesViewModel
    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!

    private var adapter: MessageAdapter? = null
    lateinit var interface_obmen:GetProfile

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModelFactory =  DaggerAppComponent.create().viewFactory()

        messagesViewModel = ViewModelProvider(this, viewModelFactory)[MessagesViewModel::class.java]
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)

        activity?.let {
            messagesViewModel.repository.setApp(it.application)
            messagesViewModel.getKey()
        }

        adapter = MessageAdapter()
        binding.listOfMessages.adapter = adapter

        binding.btnSend.setOnClickListener {

            if (messagesViewModel.controlMessage(interface_obmen.getAuth()!!, binding.textMessages.text.toString()))
                binding.textMessages.setText("")
        }

        messagesViewModel.messages?.observe(viewLifecycleOwner, {
            adapter!!.submitList(it)
        })

        val preference = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        // settings.theme  = preference.getString("theme", "light").toString()
        settings.my_color = preference.getString("my_color_message", "red").toString()
        settings.your_color = preference.getString("your_color_message", "orange").toString()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        val stopIntent = Intent(activity, MessageService::class.java)
        stopIntent.putExtra(MessageService.COMMAND_ID, MessageService.COMMAND_STOP)
        activity?.startService(stopIntent)
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        val intent =  Intent(activity, MessageService::class.java)
        intent.putExtra("key", messagesViewModel.returnLastMessage())
        intent.putExtra(MessageService.COMMAND_ID, MessageService.COMMAND_START)
        activity?.startService(intent)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GetProfile)
            interface_obmen = context
    }
}