package com.example.noteapp.data.repo

import com.example.noteapp.data.model.Note
import com.example.noteapp.data.util.FireStoreTable
import com.example.noteapp.data.util.UiState
import com.google.firebase.firestore.FirebaseFirestore

class NoteRepositoryImp(
    private val database: FirebaseFirestore
) : NoteRepository {
    override fun getNotes(result: (UiState<List<Note>>) -> Unit) {
        database.collection(FireStoreTable.NOTE)
            .get()
            .addOnSuccessListener {
                val notes = arrayListOf<Note>()
                for (document in it) {
                    val note = document.toObject(Note::class.java)
                    notes.add(note)
                }
                result.invoke(
                    UiState.Success(notes)
                )
            }
            .addOnFailureListener {
                UiState.Failure(
                    it.localizedMessage
                )
            }
    }

    override fun addNote(note: Note, result: (UiState<String>) -> Unit) {
        val document = database.collection(FireStoreTable.NOTE).document()
        note.id = document.id
        document
            .set(note)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Note has been created ")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun updateNote(note: Note, result: (UiState<String>) -> Unit) {
        val document = database.collection(FireStoreTable.NOTE).document(note.id)
        document
            .set(note)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Note has been update ")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun deleteNote(note: Note, result: (UiState<String>) -> Unit) {
        val document = database.collection(FireStoreTable.NOTE).document(note.id)
        document
            .delete()
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Note has been deleted ")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }
}