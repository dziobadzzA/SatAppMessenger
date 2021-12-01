package com.satellite.messenger.ui.messages.adapter

import android.graphics.Color
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.satellite.messenger.R
import com.satellite.messenger.databinding.ListMessagesBinding
import com.satellite.messenger.utils.settings
import com.satellite.messenger.utils.state.ServerMessage
import java.text.SimpleDateFormat
import java.util.*


class MessageViewHolder(
    private val binding: ListMessagesBinding
): RecyclerView.ViewHolder(binding.root) {

    private val requestOptions = RequestOptions()
        .downsample(DownsampleStrategy.CENTER_INSIDE)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(R.drawable.ic_baseline_architecture_24)

    fun bind(message: ServerMessage) {
        binding.messageUser.text = message.email
        binding.messageText.text = message.message
        val timeZoneDate = SimpleDateFormat("EEE, dd-MM-yyyy  hh:mm a", Locale.getDefault())

        FirebaseStorage.getInstance().reference.child("images").child(message.email).downloadUrl.addOnSuccessListener {
            Glide.with(binding.root.context)
                .load(it)
                .apply(requestOptions)
                .into(binding.icon)
        }

        if (FirebaseAuth.getInstance().currentUser?.email == message.email)
            MyStyle(settings.my_color)
        else
            YourStyle(settings.your_color)

        binding.messageTime.text = timeZoneDate.format(Date(message.datetime))
    }

    private fun style(color: Int) {
        binding.messageContainer.setBackgroundColor(color)
    }

    private fun MyStyle(my_color: String){
        when (my_color) {
            "red" -> style(Color.argb(255, 255, 50, 0))
            "cyan" -> style(Color.argb(255, 4, 133, 157))
            "blue" -> style(Color.argb(255, 21, 49, 174))
            "purple" -> style(Color.argb(255, 83, 15, 173))
        }
    }

    private fun YourStyle(my_color: String){
        when (my_color) {
            "orange" -> style(Color.argb(255, 255, 116, 0))
            "cyan" -> style(Color.argb(255, 4, 133, 157))
            "blue" -> style(Color.argb(255, 21, 49, 174))
            "magenta" -> style(Color.argb(255, 205, 0, 116))
        }
    }

}