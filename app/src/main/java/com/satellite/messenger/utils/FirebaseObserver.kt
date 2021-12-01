package com.satellite.messenger.utils

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.satellite.messenger.utils.state.ServerMessage

class FirebaseObserver(mes:(ServerMessage) -> Unit) {

    init {
        FirebaseDatabase.getInstance().reference.child("text").limitToLast(50).addChildEventListener(object:
            ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val temp = snapshot.getValue(ServerMessage::class.java)
                temp?.let { mes(it) }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // TODO
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // TODO
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // TODO
            }

            override fun onCancelled(error: DatabaseError) {
                // TODO
            }

        })
    }

}