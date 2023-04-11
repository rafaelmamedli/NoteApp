package com.example.noteapp.data.repo

import com.example.noteapp.data.model.User
import com.example.noteapp.data.util.UiState

interface AuthRepository {

    fun loginUser(user: User, result: (UiState<String>) -> Unit)
    fun updateUser(user: User, result: (UiState<String>) -> Unit)
    fun registerUser(email:String,password: String,user: User, result: (UiState<String>) -> Unit)
    fun forgotPassword(user: User, result: (UiState<String>) -> Unit)



}