package com.satellite.messenger.ui.feedback

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class FeedbackViewModel @Inject constructor(): ViewModel() {

    fun sendFeedback(email_text:String, text:String):Boolean {

        val textSendFeedback = "$email_text: $text"
        if (textSendFeedback != ": ") {
            FirebaseDatabase.getInstance().reference.child("Feedback").push().setValue(
                textSendFeedback
            )
            return true
        }

        return false
    }
}