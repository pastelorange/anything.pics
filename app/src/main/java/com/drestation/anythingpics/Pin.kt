package com.drestation.anythingpics

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp

data class Pin(
    var title: String? = null,
    var caption: String? = null,
    var imageUrl: String? = null,
    var uid: String? = null,
    var timestamp: Timestamp? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Timestamp::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(caption)
        parcel.writeString(imageUrl)
        parcel.writeString(uid)
        parcel.writeParcelable(timestamp, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pin> {
        override fun createFromParcel(parcel: Parcel): Pin {
            return Pin(parcel)
        }

        override fun newArray(size: Int): Array<Pin?> {
            return arrayOfNulls(size)
        }
    }
}