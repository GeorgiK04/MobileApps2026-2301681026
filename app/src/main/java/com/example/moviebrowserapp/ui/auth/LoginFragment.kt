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
import com.example.moviebrowser.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = requireActivity().application as MovieApp

        // Ако вече е логнат → директно към home
        if (app.sessionManager.isLoggedIn()) {
            findNavController().navigate(R.id.action_login_to_home)
            return
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            binding.tilEmail.error = null
            binding.tilPassword.error = null
            binding.tvError.visibility = View.GONE

            when {
                email.isEmpty() -> binding.tilEmail.error = "Въведи имейл"
                password.isEmpty() -> binding.tilPassword.error = "Въведи парола"
                else -> {
                    lifecycleScope.launch {
                        val user = app.repository.login(email, password)
                        if (user != null) {
                            app.sessionManager.saveUser(user.id, user.name, user.email)
                            findNavController().navigate(R.id.action_login_to_home)
                        } else {
                            binding.tvError.visibility = View.VISIBLE
                            binding.tvError.text = "Грешен имейл или парола"
                        }
                    }
                }
            }
        }

        binding.btnGoRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}