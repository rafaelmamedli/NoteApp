package com.example.noteapp.data.repo

import com.example.noteapp.data.model.User
import com.example.noteapp.data.util.UiState

interface AuthRepository {

    fun registerUser(email:String,password: String,user: User, result: (UiState<String>) -> Unit)
    fun updateUser(user: User, result: (UiState<String>) -> Unit)
    fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit)
    fun forgotPassword(email: String, result: (UiState<String>) -> Unit)
    fun logout(result: () -> Unit)
    fun storeSession (id: String, result: (User?) -> Unit)
    fun getSession(result: (User?) -> Unit)




}