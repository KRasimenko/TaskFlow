package com.practicum.taskmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.taskmanager.R
import com.practicum.taskmanager.TaskManagerApp
import com.practicum.taskmanager.data.TaskStatus
import com.practicum.taskmanager.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repo = (requireActivity().application as TaskManagerApp).repository
        val user = repo.currentUser ?: run {
            binding.textAvatar.text = "?"
            binding.textName.text = getString(R.string.guest)
            binding.textEmail.text = ""
            binding.statTasks.text = "0"
            binding.statDone.text = "0"
            binding.btnLogout.setOnClickListener {
                findNavController().navigate(R.id.action_profile_to_login)
            }
            return
        }
        val tasks = repo.getTasks()

        binding.textAvatar.text = user.name.firstOrNull()?.uppercase() ?: "?"
        binding.textName.text = user.name
        binding.textEmail.text = user.email
        binding.statTasks.text = tasks.size.toString()
        binding.statDone.text = tasks.count { it.status == TaskStatus.DONE }.toString()

        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_settings)
        }
        binding.btnSchedule.setOnClickListener {
            findNavController().navigate(R.id.scheduleFragment)
        }
        binding.btnFavorites.setOnClickListener {
            findNavController().navigate(R.id.favoritesFragment)
        }
        binding.btnHelp.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_help)
        }
        binding.btnLogout.setOnClickListener {
            repo.logout()
            findNavController().navigate(R.id.action_profile_to_login)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
