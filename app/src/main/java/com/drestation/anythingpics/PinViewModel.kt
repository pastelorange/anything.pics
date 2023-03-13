package com.drestation.anythingpics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

// This class queries the database and adds each document into a list
class PinViewModel: ViewModel() {
    private var pins = MutableLiveData<List<Pin>>()

    init {
        //val uid = Firebase.auth.currentUser?.uid

        // Build a query to get the document from "projects"
        val db = FirebaseFirestore.getInstance().collection("pinboard")
            .orderBy("title")
            .addSnapshotListener { documents, _ ->
                // If documents is not null
                documents?.let {
                    val pinList = ArrayList<Pin>()

                    // Foreach document in documents, add it to the list
                    for (document in it) {
                        val pin = document.toObject(Pin::class.java)
                        pinList.add(pin)
                    }
                    pins.value = pinList
                }
            }
    }

    // Getter to access these retrieved pins
    fun getPins(): LiveData<List<Pin>> {
        return pins
    }
}