package com.practicum.taskmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.taskmanager.R
import com.practicum.taskmanager.TaskManagerApp
import com.practicum.taskmanager.databinding.FragmentSignupBinding

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repo = (requireActivity().application as TaskManagerApp).repository

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSignup.setOnClickListener {
            val name = binding.inputName.text?.toString()?.trim().orEmpty()
            val email = binding.inputEmail.text?.toString()?.trim().orEmpty()
            if (email.isEmpty()) return@setOnClickListener
            repo.login(email, name)
            findNavController().navigate(R.id.action_signup_to_home)
        }

        binding.linkLogin.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
