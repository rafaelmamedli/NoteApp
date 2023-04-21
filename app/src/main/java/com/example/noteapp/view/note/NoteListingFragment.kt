package com.example.noteapp.view.note

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.firebasewithmvvm.note.NoteListingAdapter
import com.example.noteapp.data.model.Note
import com.example.noteapp.data.util.UiState
import com.example.noteapp.data.util.hide
import com.example.noteapp.data.util.toast
import com.example.mvvm_firestpre_mvvm.R
import com.example.mvvm_firestpre_mvvm.databinding.FragmentNoteListeningBinding
import com.example.noteapp.viewmodel.AuthViewModel
import com.example.noteapp.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListingFragment : Fragment() {
    lateinit var binding: FragmentNoteListeningBinding
    private val viewModel: NoteViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    var deletePosition: Int = -1
    var list: MutableList<Note> = arrayListOf()
    private val adapter by lazy {
        NoteListingAdapter(
            onItemClicked = { _, item ->
                findNavController().navigate(
                    R.id.action_noteListeningFragment_to_noteDetailFragment,
                    Bundle().apply {
                        putString("type", "view")
                        putParcelable("note", item)

                    })
            },
            onEditClicked = { _, item ->
                findNavController().navigate(
                    R.id.action_noteListeningFragment_to_noteDetailFragment,
                    Bundle().apply {
                        putString("type", "edit")
                        putParcelable("note", item)

                    })
            },
            onDeleteClicked = { pos, item ->
                deletePosition = pos
                viewModel.deleteNote(item)

            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteListeningBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.itemAnimator = null
        binding.button.setOnClickListener {
            findNavController().navigate(
                R.id.action_noteListeningFragment_to_noteDetailFragment,
                Bundle().apply {
                    putString("type", "create")
                })
        }
        observer()

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar2)
        setHasOptionsMenu(true)


        authViewModel.getSession {
            viewModel.getNotes(it)
        }




        viewModel.deleteNote.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    toast(state.data)
                    adapter.removeItem(deletePosition)

                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    state.error?.let { toast(it) }
                }

            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.logOut -> {
                authViewModel.logout {
                    findNavController().navigate(R.id.action_noteListeningFragment_to_loginFragment)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observer() {
        viewModel.note.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    adapter.updateList(state.data.toMutableList())
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    state.error?.let { toast(it) }
                }

            }

        }    }


}