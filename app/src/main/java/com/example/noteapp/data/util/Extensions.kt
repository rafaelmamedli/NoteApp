package com.example.noteapp.data.util

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

fun View.hide(){
    visibility = View.GONE
}

fun View.show(){
    visibility = View.VISIBLE
}

fun View.gone(){
    visibility = View.GONE
}

fun Fragment.toast(msg:String){
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun goTo(it: View, id: Int) {
    Navigation.findNavController(it).navigate(id)
}

fun String.isValidEmail()=
    isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()