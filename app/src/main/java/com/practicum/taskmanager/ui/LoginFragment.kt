package com.practicum.taskmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.taskmanager.R
import com.practicum.taskmanager.TaskManagerApp
import com.practicum.taskmanager.databinding.FragmentLoginBinding

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

        if (repo.isLoggedIn()) {
            findNavController().navigate(R.id.action_login_to_home)
            return
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.inputEmail.text?.toString()?.trim().orEmpty()
            if (email.isEmpty()) {
                binding.inputEmail.error = getString(R.string.email)
                return@setOnClickListener
            }
            repo.login(email)
            findNavController().navigate(R.id.action_login_to_home)
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
