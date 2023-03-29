package com.drestation.anythingpics

import com.google.firebase.Timestamp

class Pin(
    var title: String? = null,
    var caption: String? = null,
    var imageUrl: String? = null,
    var uid: String? = null,
    var timestamp: Timestamp? = null
)