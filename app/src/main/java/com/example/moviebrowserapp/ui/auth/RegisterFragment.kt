package com.example.moviebrowser.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.moviebrowser.MovieApp
import com.example.moviebrowser.R
import com.example.moviebrowser.data.local.UserEntity
import com.example.moviebrowser.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = requireActivity().application as MovieApp

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirm = binding.etConfirmPassword.text.toString().trim()

            binding.tilName.error = null
            binding.tilEmail.error = null
            binding.tilPassword.error = null
            binding.tilConfirmPassword.error = null
            binding.tvError.visibility = View.GONE

            when {
                name.isEmpty() -> binding.tilName.error = "Въведи име"
                email.isEmpty() -> binding.tilEmail.error = "Въведи имейл"
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    binding.tilEmail.error = "Невалиден имейл"
                password.length < 6 ->
                    binding.tilPassword.error = "Паролата трябва да е поне 6 символа"
                password != confirm ->
                    binding.tilConfirmPassword.error = "Паролите не съвпадат"
                else -> {
                    lifecycleScope.launch {
                        val existing = app.repository.getUserByEmail(email)
                        if (existing != null) {
                            binding.tilEmail.error = "Този имейл вече е регистриран"
                            return@launch
                        }
                        val userId = app.repository.register(
                            UserEntity(name = name, email = email, password = password)
                        )
                        app.sessionManager.saveUser(userId.toInt(), name, email)
                        findNavController().navigate(R.id.action_register_to_login)
                    }
                }
            }
        }

        binding.btnGoLogin.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_login)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}