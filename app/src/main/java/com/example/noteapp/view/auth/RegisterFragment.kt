package com.example.noteapp.view.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mvvm_firestpre_mvvm.R
import com.example.mvvm_firestpre_mvvm.databinding.FragmentRegisterBinding
import com.example.noteapp.data.model.User
import com.example.noteapp.data.util.*
import com.example.noteapp.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterFragment : Fragment() {
    lateinit var binding: FragmentRegisterBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        binding.createAccount.setOnClickListener {
            if (validation()){
                viewModel.register(
                    binding.email.text.toString(),
                    binding.password.text.toString(),
                    user = getUserObj()
                )
            }
        }
    }

    private fun observer() {
        viewModel.register.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
                    binding.registerProgress.show()
                    binding.createAccount.text = " "
                }
                is UiState.Failure -> {
                    binding.registerProgress.hide()
                }
                is UiState.Success -> {
                    binding.registerProgress.hide()
                    toast(state.data)
                    findNavController().navigate(R.id.action_registerFragment_to_noteListeningFragment)


                }
            }
        }
    }

    private fun getUserObj(): User {
        return User(
            id = "",
            user_name = binding.userName.text.toString(),
            email = binding.email.text.toString(),
        )
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.userName.text.isNullOrEmpty()){
            isValid = false
        }


        if (binding.email.text.isNullOrEmpty()){
            isValid = false

        }else{
            if (!binding.email.text.toString().isValidEmail()){
                isValid = false

            }
        }
        if (binding.password.text.isNullOrEmpty()){
            isValid = false

        }else{
            if (binding.password.text.toString().length < 6){
                isValid = false
            }
        }
        return isValid
    }

}