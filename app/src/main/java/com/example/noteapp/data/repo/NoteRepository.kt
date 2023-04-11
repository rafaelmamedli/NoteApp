package com.example.noteapp.data.repo

import com.example.noteapp.data.model.Note
import com.example.noteapp.data.util.UiState

interface NoteRepository {

    fun getNotes(result: (UiState<List<Note>>) -> Unit)
    fun addNote(note: Note, result: (UiState<String>) -> Unit)
    fun updateNote(note: Note, result: (UiState<String>) -> Unit)
    fun deleteNote(note: Note, result: (UiState<String>) -> Unit)


}