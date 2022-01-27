package com.example.myapplication

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Options(val email: String) : Parcelable{
    companion object{
        @JvmStatic val DEFAULT = Options(email = "tima.galimov.2019@mail.ru")
    }
}
