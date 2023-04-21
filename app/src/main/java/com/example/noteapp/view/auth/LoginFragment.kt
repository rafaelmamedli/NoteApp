package com.example.noteapp.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.mvvm_firestpre_mvvm.R
import com.example.mvvm_firestpre_mvvm.databinding.FragmentLoginBinding
import com.example.noteapp.data.util.*
import com.example.noteapp.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        binding.loginButton.setOnClickListener {
            if (validation()) {
                viewModel.login(
                    email = binding.emailLogin.text.toString(),
                    password = binding.passwordLogin.text.toString()
                )
            }
        }

        binding.haventRegistered.setOnClickListener {
            goTo(it, R.id.action_loginFragment_to_registerFragment)
        }
        binding.forgotPassword.setOnClickListener {
            goTo(it,R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }


    private fun observer() {
        viewModel.login.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.loginButton.text = ""
                    binding.loginProgress.show()
                    toast("Loas")
                }
                is UiState.Failure -> {
                    binding.loginButton.text = "Login"
                    binding.loginProgress.hide()
                    state.error?.let { toast(it) }
                }
                is UiState.Success -> {
                    binding.loginButton.text = "Login"
                    binding.loginProgress.hide()
                    toast(state.data)
                    findNavController().navigate(R.id.action_loginFragment_to_noteListeningFragment)
                }
            }
        }
    }


    private fun validation(): Boolean {
        if (binding.emailLogin.text.isNullOrEmpty()) {
            toast("Enter e-mail adress")
            return false
        }

        if (!binding.emailLogin.text.toString().isValidEmail()) {
            toast("Invalid email entered")
            return false
        }

        if (binding.passwordLogin.text.isNullOrEmpty()) {
            toast("Enter password")
            return false
        }

        if (binding.passwordLogin.text.toString().length < 6) {
            toast("Password is short")
            return false
        }

        return true
    }


    override fun onStart() {
        super.onStart()
        viewModel.getSession { user ->
            if (user != null){
                findNavController().navigate(R.id.action_loginFragment_to_noteListeningFragment)
            }
        }
    }

}