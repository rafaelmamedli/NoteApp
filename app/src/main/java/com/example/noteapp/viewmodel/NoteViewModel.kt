package com.example.noteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.model.Note
import com.example.noteapp.data.model.User
import com.example.noteapp.data.repo.NoteRepository
import com.example.noteapp.data.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository
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


    fun getNotes(user: User?) = viewModelScope.launch(Dispatchers.Main) {
        _notes.value = UiState.Loading
        repository.getNotes(user) { result ->
            _notes.value = result }

    }




    fun addNote(note: Note) = viewModelScope.launch(Dispatchers.Main) {
        _addNotes.value = UiState.Loading
        repository.addNote(note) {result ->
            _addNotes.value = result }
    }


    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.Main)
    {
        _updateNotes.value = UiState.Loading
        repository.updateNote(note) {result ->
            _updateNotes.value = result }
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.Main) {
        _deleteNotes.value = UiState.Loading
        repository.deleteNote(note) { result ->
            _deleteNotes.value = result }
    }


}