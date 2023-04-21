package com.example.noteapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class User(
    var id: String = "",
    val user_name:String ="",
    val email:String = ""
)
