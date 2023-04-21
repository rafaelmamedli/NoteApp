package com.example.noteapp.view.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mvvm_firestpre_mvvm.R
import com.example.noteapp.data.model.Note
import com.example.noteapp.data.util.UiState
import com.example.noteapp.data.util.hide
import com.example.noteapp.data.util.toast
import com.example.mvvm_firestpre_mvvm.databinding.FragmentNoteDetailBinding
import com.example.noteapp.viewmodel.AuthViewModel
import com.example.noteapp.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class NoteDetailFragment : Fragment() {

    private val viewModel: NoteViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
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
        if (validation()) {
            viewModel.updateNote(
                Note(
                    id = objectNote?.id ?: "",
                    text = binding.editText.text.toString(),
                    date = Date(),
                    title = binding.titleEt.text.toString()
                ).apply { authViewModel.getSession { this.user_id = it?.id ?: "" } }
            )
        }

        viewModel.updateNote.observe(viewLifecycleOwner) { state ->
            when (state) {

                is UiState.Loading -> {
                    binding.progressBarDetail.visibility = View.VISIBLE
                    binding.btnUpdate.text = ""
                }
                is UiState.Success -> {
                    binding.progressBarDetail.hide()
                    toast(state.data)
                    binding.btnUpdate.text = "Update"


                }
                is UiState.Failure -> {
                    binding.btnUpdate.text = "Update"
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
                    id = "",
                    text = binding.editText.text.toString(),
                    date = Date(),
                    title = binding.titleEt.text.toString()
                ).apply { authViewModel.getSession { this.user_id = it?.id ?: "" } }
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
                    binding.btnUpdate.text = "Create Note"
                    findNavController().navigate(R.id.action_noteDetailFragment_to_noteListeningFragment)
                    toast(state.data)

                }
                is UiState.Failure -> {
                    binding.progressBarDetail.hide()
                    binding.btnUpdate.text = "Create Note"
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
                    binding.btnUpdate.text = "Update"

                }
            }
        }

    }

    private fun validation(): Boolean {
        val message = binding.editText.text.toString()
        if (message.isNullOrEmpty()) {
            toast("Enter a message")
            return false
        }
        return true
    }


}