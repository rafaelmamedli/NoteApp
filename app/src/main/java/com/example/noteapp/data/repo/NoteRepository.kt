package com.example.noteapp.data.repo

import com.example.noteapp.data.model.Note
import com.example.noteapp.data.model.User
import com.example.noteapp.data.util.UiState

interface NoteRepository {

   suspend fun getNotes(user: User?,result: (UiState<List<Note>>) -> Unit)
   suspend fun addNote(note: Note, result: (UiState<String>) -> Unit)
   suspend fun updateNote(note: Note, result: (UiState<String>) -> Unit)
   suspend fun deleteNote(note: Note, result: (UiState<String>) -> Unit)


}