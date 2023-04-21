package com.example.noteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteapp.data.model.Note
import com.example.noteapp.data.model.User
import com.example.noteapp.data.repo.NoteRepository
import com.example.noteapp.data.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    val repository: NoteRepository
) : ViewModel() {


    private val _notes = MutableLiveData<UiState<List<Note>>>()
    val note: LiveData<UiState<List<Note>>>
        get() = _notes

    private val _addNotes = MutableLiveData<UiState<String>>()
    val addNote: LiveData<UiState<String>>
        get() = _addNotes

    private val _updateNotes = MutableLiveData<UiState<String>>()
    val updateNote: LiveData<UiState<String>>
        get() = _updateNotes

    private val _deleteNotes = MutableLiveData<UiState<String>>()
    val deleteNote: LiveData<UiState<String>>
        get() = _deleteNotes


    fun getNotes(user: User?) {
        _notes.value = UiState.Loading
        repository. getNotes(user) {  _notes.value = it }
    }

    fun addNote(note : Note){
        _addNotes.value = UiState.Loading
        repository.addNote(note){ _addNotes.value = it}
    }

    fun updateNote(note : Note){
        _updateNotes.value = UiState.Loading
        repository.updateNote(note){ _updateNotes.value = it}
    }
    fun deleteNote(note : Note){
        _deleteNotes.value = UiState.Loading
        repository.deleteNote(note){ _deleteNotes.value = it}
    }

}