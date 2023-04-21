package com.example.noteapp.view.note

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mvvm_firestpre_mvvm.R
import com.example.mvvm_firestpre_mvvm.databinding.FragmentNoteDetailBinding
import com.example.noteapp.data.model.Note
import com.example.noteapp.data.util.TAG
import com.example.noteapp.data.util.UiState
import com.example.noteapp.data.util.toast
import com.example.noteapp.viewmodel.AuthViewModel
import com.example.noteapp.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class NoteDetailFragment : Fragment() {

    private val viewModel: NoteViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    lateinit var binding: FragmentNoteDetailBinding
    private var isEdit = false
    var objectNote: Note? = null
    var item: MenuItem? = null

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
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar2)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
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
                }
                is UiState.Success -> {
                    Log.e(TAG, state.data)
                }
                is UiState.Failure -> {
                    state.error?.let { Log.d(TAG, it) }
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
                }
                is UiState.Success -> {
                    Log.e(TAG, state.data)
                }
                is UiState.Failure -> {
                    state.error?.let { Log.d(TAG, it) }
                }

            }
        }
    }

    private fun updateUI() {
        val type = arguments?.getString("type", null)
        type?.let {
            when (it) {
                "view" -> {
                    item?.isVisible = false
                    isEdit = false
                    binding.editText.isEnabled = false
                    binding.titleEt.isEnabled = false
                    objectNote = arguments?.getParcelable("note")
                    binding.editText.setText(objectNote?.text)
                    binding.titleEt.setText(objectNote?.title)
                }
                "create" -> {
                    isEdit = false
                }
                "edit" -> {
                    isEdit = true
                    objectNote = arguments?.getParcelable("note")
                    binding.editText.setText(objectNote?.text)
                    binding.titleEt.setText(objectNote?.title)
                }
            }
        }

    }

    private fun validation(): Boolean {
        val message = binding.editText.text.toString()
        if (message.isEmpty()) {
            toast("Enter a message")
            return false
        }
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val type = arguments?.getString("type", null)
        if (type == "view") {
            menu.findItem(R.id.save_btn)?.isVisible = false
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        item = menu.findItem(R.id.save_btn)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                return true
            }
            R.id.save_btn -> {
                if (validation()) {
                    findNavController().navigate(R.id.action_noteDetailFragment_to_noteListeningFragment)
                }
                if (isEdit) {
                    updateNote()
                } else {
                    createNote()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }





}