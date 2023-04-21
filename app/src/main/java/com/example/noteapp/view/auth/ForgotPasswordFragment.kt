package com.example.noteapp.view.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mvvm_firestpre_mvvm.databinding.FragmentForgotPasswordBinding
import com.example.noteapp.data.util.*
import com.example.noteapp.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {
    lateinit var binding: FragmentForgotPasswordBinding
    private val viewModel: AuthViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        binding.forgotButton.setOnClickListener {
            if (validation()) {
                viewModel.forgotPassword(binding.emailForgotten.text.toString())
            }
        }
    }



    private fun observer(){
        viewModel.forgotPassword.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
                    binding.forgotButton.text = ""
                    binding.forgotProgress.show()
                }
                is UiState.Failure -> {
                    binding.forgotButton.text = "Send"
                    binding.forgotProgress.hide()
                    state.error?.let { toast(it) }
                }
                is UiState.Success -> {
                    binding.forgotButton.text = "Send"
                    binding.forgotProgress.hide()
                    toast(state.data)
                }
            }
        }
    }




    fun validation(): Boolean {
        var isValid = true

        if (binding.emailForgotten.text.isNullOrEmpty()){
            isValid = false
            toast("Enter e-mail")
        }else{
            if (!binding.emailForgotten.text.toString().isValidEmail()){
                isValid = false
                toast("Invalid Email")
            }
        }

        return isValid
    }
}