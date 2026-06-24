package com.practicum.taskmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.taskmanager.R
import com.practicum.taskmanager.TaskManagerApp
import com.practicum.taskmanager.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repo = (requireActivity().application as TaskManagerApp).repository

        binding.btnLogin.setOnClickListener {
            val email = binding.inputEmail.text?.toString()?.trim().orEmpty()
            val password = binding.inputPassword.text?.toString().orEmpty()

            if (email.isEmpty()) {
                Toast.makeText(requireContext(), R.string.error_empty_email, Toast.LENGTH_SHORT).show()
                binding.inputEmail.requestFocus()
                return@setOnClickListener
            }

            binding.btnLogin.isEnabled = false
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    if (password.length >= 8) {
                        repo.login(email, password)
                    } else {
                        repo.loginLocal(email)
                    }
                    findNavController().navigate(R.id.action_login_to_home)
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_login, e.message ?: ""),
                        Toast.LENGTH_LONG,
                    ).show()
                } finally {
                    _binding?.btnLogin?.isEnabled = true
                }
            }
        }

        binding.linkSignup.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_signup)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
