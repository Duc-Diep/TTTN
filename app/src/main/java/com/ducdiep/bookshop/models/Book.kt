package com.ducdiep.bookshop.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Book(
    var id: Int,
    var imageLink: String,
    var title: String,
    var author: String,
    var publisher: String,
    var releaseYear: Int,
    var numOfPage: Int,
    var price: Double,
    var rateStar: Double,
    var numOfReview: Int,
    var description: String,
    var category: String,
    var dateBuy: String
):Serializable,Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(imageLink)
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(publisher)
        parcel.writeInt(releaseYear)
        parcel.writeInt(numOfPage)
        parcel.writeDouble(price)
        parcel.writeDouble(rateStar)
        parcel.writeInt(numOfReview)
        parcel.writeString(description)
        parcel.writeString(category)
        parcel.writeString(dateBuy)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }
}
