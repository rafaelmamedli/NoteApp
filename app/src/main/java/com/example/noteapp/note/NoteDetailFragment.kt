package com.example.noteapp.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.noteapp.data.model.Note
import com.example.noteapp.util.UiState
import com.example.noteapp.util.hide
import com.example.noteapp.util.toast
import com.example.mvvm_firestpre_mvvm.databinding.FragmentNoteDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class NoteDetailFragment : Fragment() {

    private val viewModel: NoteViewModel by viewModels()
    lateinit var binding: FragmentNoteDetailBinding
    var isEdit = false
    var objectNote: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()

        binding.btnUpdate.setOnClickListener {
            if (isEdit) {
                updateNote()
            } else {
                createNote()
            }
        }


    }


    private fun updateNote() {
        if (validation()){
            viewModel.updateNote(
                Note(
                    id = objectNote?.id ?: "",
                    binding.editText.text.toString(),
                    Date()
                )
            )
        }

        viewModel.updateNote.observe(viewLifecycleOwner){ state ->
            when(state){

                is UiState.Loading -> {
                    binding.progressBarDetail.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    binding.progressBarDetail.hide()
                    toast(state.data)

                }
                is UiState.Failure ->{
                    binding.progressBarDetail.hide()
                    state.error?.let { toast(it) }
                }

            }

        }

    }

    private fun createNote() {
        if (validation()) {
            viewModel.addNote(
                Note(
                    id = "" ,
                    binding.editText.text.toString(),
                    Date()
                )
            )
        }

        viewModel.addNote.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBarDetail.visibility = View.VISIBLE
                    binding.btnUpdate.text = ""
                }
                is UiState.Success -> {
                    binding.progressBarDetail.hide()
                    binding.btnUpdate.text = "Update"
                    toast(state.data)

                }
                is UiState.Failure -> {
                    binding.progressBarDetail.hide()
                    binding.btnUpdate.text = "Update"
                    state.error?.let { toast(it) }
                }

            }
        }
    }


    private fun updateUI() {
        val type = arguments?.getString("type", null)
        type?.let {
            when (it) {
                "view" -> {
                    isEdit = false
                    binding.editText.isEnabled = false
                    objectNote = arguments?.getParcelable("note")
                    binding.editText.setText(objectNote?.text)
                    binding.btnUpdate.hide()
                }
                "create" -> {
                    isEdit = false
                    binding.btnUpdate.text = "Create"

                }
                "edit" -> {
                    isEdit = true
                    objectNote = arguments?.getParcelable("note")
                    binding.editText.setText(objectNote?.text)
                    binding.btnUpdate.text = "update"

                }
            }
        }

    }

    private fun validation(): Boolean {
        var isValid = true
        if (binding.editText.text.toString().isNullOrEmpty()) {
            isValid = false
            toast("enter message")
        }
        return isValid
    }


}