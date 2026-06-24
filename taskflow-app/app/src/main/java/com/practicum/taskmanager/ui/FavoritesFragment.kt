package com.practicum.taskmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.taskmanager.R
import com.practicum.taskmanager.TaskManagerApp
import com.practicum.taskmanager.databinding.FragmentFavoritesBinding
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repo = (requireActivity().application as TaskManagerApp).repository

        adapter = TaskAdapter(
            onTaskClick = { task ->
                findNavController().navigate(
                    R.id.taskDetailFragment,
                    Bundle().apply { putString("taskId", task.id) },
                )
            },
            onStarClick = { task ->
                viewLifecycleOwner.lifecycleScope.launch {
                    repo.toggleStar(task.id)
                    refresh(repo)
                }
            },
        )
        binding.recyclerTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerTasks.adapter = adapter
        refresh(repo)
    }

    override fun onResume() {
        super.onResume()
        val repo = (requireActivity().application as TaskManagerApp).repository
        viewLifecycleOwner.lifecycleScope.launch {
            runCatching { repo.syncFromServer() }
            refresh(repo)
        }
    }

    private fun refresh(repo: com.practicum.taskmanager.data.TaskRepository) {
        val binding = _binding ?: return
        val starred = repo.getStarredTasks()
        adapter.submitList(starred)
        binding.textEmpty.visibility = if (starred.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
