package com.practicum.taskmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.practicum.taskmanager.R
import com.practicum.taskmanager.TaskManagerApp
import com.practicum.taskmanager.data.TaskStatus
import com.practicum.taskmanager.databinding.FragmentHomeBinding
import com.practicum.taskmanager.util.DateUtils
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TaskAdapter
    private var filter: TaskStatus? = null
    private var query: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repo = (requireActivity().application as TaskManagerApp).repository

        binding.textUserName.text = repo.currentUser?.name ?: getString(R.string.guest)

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
                    refreshList(repo)
                }
            },
        )
        binding.recyclerTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerTasks.adapter = adapter

        binding.btnNotifications.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_notifications)
        }

        binding.inputSearch.doAfterTextChanged {
            query = it?.toString()?.trim().orEmpty().lowercase()
            refreshList(repo)
        }

        setupFilterChips(repo)
        refreshList(repo)
    }

    override fun onResume() {
        super.onResume()
        val repo = (requireActivity().application as TaskManagerApp).repository
        _binding?.textUserName?.text = repo.currentUser?.name ?: getString(R.string.guest)
        viewLifecycleOwner.lifecycleScope.launch {
            runCatching { repo.syncFromServer() }
            refreshList(repo)
        }
    }

    private fun setupFilterChips(repo: com.practicum.taskmanager.data.TaskRepository) {
        val chips = listOf(
            binding.chipAll to null,
            binding.chipTodo to TaskStatus.TODO,
            binding.chipProgress to TaskStatus.IN_PROGRESS,
            binding.chipDone to TaskStatus.DONE,
        )
        chips.forEach { (chip, status) ->
            chip.setOnCheckedChangeListener { _, checked ->
                if (checked) {
                    filter = status
                    refreshList(repo)
                }
            }
        }
    }

    private fun refreshList(repo: com.practicum.taskmanager.data.TaskRepository) {
        val binding = _binding ?: return
        val tasks = repo.getTasks()
        val today = DateUtils.todayIso()

        binding.statTotal.text = tasks.size.toString()
        binding.statToday.text = tasks.count { it.dueDate == today }.toString()
        binding.statDone.text = tasks.count { it.status == TaskStatus.DONE }.toString()

        val filtered = tasks.filter { task ->
            val matchFilter = filter == null || task.status == filter
            val matchQuery = query.isEmpty() ||
                task.title.lowercase().contains(query) ||
                task.category.lowercase().contains(query)
            matchFilter && matchQuery
        }

        adapter.submitList(filtered)
        binding.textEmpty.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
        binding.recyclerTasks.visibility = if (filtered.isEmpty()) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
