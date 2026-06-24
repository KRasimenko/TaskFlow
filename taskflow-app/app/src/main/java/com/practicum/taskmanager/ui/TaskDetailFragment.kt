package com.practicum.taskmanager.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.practicum.taskmanager.R
import com.practicum.taskmanager.TaskManagerApp
import com.practicum.taskmanager.data.TaskPriority
import com.practicum.taskmanager.data.TaskStatus
import com.practicum.taskmanager.databinding.FragmentTaskDetailBinding
import com.practicum.taskmanager.util.DateUtils
import com.practicum.taskmanager.util.TaskUiHelper
import java.util.Calendar
import kotlinx.coroutines.launch

class TaskDetailFragment : Fragment() {

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!

    private val taskId: String
        get() = arguments?.getString("taskId") ?: "new"

    private val isNew: Boolean get() = taskId == "new"

    private var dueDate: String = DateUtils.todayIso()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repo = (requireActivity().application as TaskManagerApp).repository
        val task = if (isNew) null else repo.getTask(taskId)

        if (!isNew && task == null) {
            findNavController().navigateUp()
            return
        }

        setupSpinners()
        setupDatePicker()

        if (task != null) {
            dueDate = task.dueDate
            binding.heroCard.visibility = View.VISIBLE
            binding.textPageTitle.visibility = View.GONE
            binding.heroCategory.text = task.category
            binding.heroTitle.text = task.title
            binding.inputTitle.setText(task.title)
            binding.inputDescription.setText(task.description)
            binding.inputCategory.setText(task.category)
            binding.spinnerPriority.setSelection(task.priority.ordinal)
            binding.spinnerStatus.setSelection(task.status.ordinal)
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnStar.visibility = View.VISIBLE
            updateStar(task.starred)
            binding.quickStatusScroll.visibility = View.VISIBLE
            setupQuickStatus(repo, task.id, task.status)
            binding.btnSave.text = getString(R.string.save)
        } else {
            binding.heroCard.visibility = View.GONE
            binding.textPageTitle.visibility = View.VISIBLE
            binding.btnDelete.visibility = View.GONE
            binding.btnStar.visibility = View.GONE
            binding.quickStatusScroll.visibility = View.GONE
            binding.btnSave.text = getString(R.string.create)
        }

        binding.inputDueDate.setText(DateUtils.formatDateRu(dueDate))

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.btnStar.setOnClickListener {
            task?.let { t ->
                viewLifecycleOwner.lifecycleScope.launch {
                    repo.toggleStar(t.id)
                    val updated = repo.getTask(t.id)
                    if (updated != null) {
                        _binding?.let { updateStar(updated.starred) }
                    }
                }
            }
        }

        binding.btnSave.setOnClickListener {
            val title = binding.inputTitle.text?.toString()?.trim().orEmpty()
            if (title.isEmpty()) {
                binding.inputTitle.error = getString(R.string.title)
                return@setOnClickListener
            }
            val description = binding.inputDescription.text?.toString().orEmpty()
            val category = binding.inputCategory.text?.toString()?.trim().orEmpty().ifBlank { "Работа" }
            val priority = TaskPriority.entries[binding.spinnerPriority.selectedItemPosition]
            val status = TaskStatus.entries[binding.spinnerStatus.selectedItemPosition]

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    if (isNew) {
                        repo.createTask(title, description, category, priority, status, dueDate)
                    } else if (task != null) {
                        repo.updateTask(task.id) {
                            it.copy(
                                title = title,
                                description = description,
                                category = category,
                                priority = priority,
                                status = status,
                                dueDate = dueDate,
                            )
                        }
                    }
                    if (viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                        findNavController().navigateUp()
                    }
                } catch (e: Exception) {
                    if (_binding != null) {
                        android.widget.Toast.makeText(
                            requireContext(),
                            getString(R.string.error_network, e.message ?: ""),
                            android.widget.Toast.LENGTH_LONG,
                        ).show()
                    }
                }
            }
        }

        binding.btnDelete.setOnClickListener {
            if (task == null) return@setOnClickListener
            AlertDialog.Builder(requireContext())
                .setMessage(R.string.delete_confirm)
                .setPositiveButton(R.string.delete) { _, _ ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        repo.deleteTask(task.id)
                        if (viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                            findNavController().navigateUp()
                        }
                    }
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }
    }

    private fun setupSpinners() {
        val priorities = TaskPriority.entries.map {
            TaskUiHelper.priorityLabel(requireContext(), it)
        }
        val statuses = TaskStatus.entries.map {
            TaskUiHelper.statusLabel(requireContext(), it)
        }
        binding.spinnerPriority.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            priorities,
        )
        binding.spinnerStatus.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            statuses,
        )
    }

    private fun setupDatePicker() {
        binding.inputDueDate.setOnClickListener {
            val cal = DateUtils.parseIso(dueDate)
            DatePickerDialog(
                requireContext(),
                { _, y, m, d ->
                    val picked = Calendar.getInstance().apply { set(y, m, d) }
                    dueDate = DateUtils.toIsoDate(picked)
                    binding.inputDueDate.setText(DateUtils.formatDateRu(dueDate))
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH),
            ).show()
        }
    }

    private fun setupQuickStatus(
        repo: com.practicum.taskmanager.data.TaskRepository,
        id: String,
        current: TaskStatus,
    ) {
        binding.quickStatusChips.removeAllViews()
        TaskStatus.entries.forEach { status ->
            val chip = Chip(requireContext()).apply {
                text = TaskUiHelper.statusShortLabel(requireContext(), status)
                isCheckable = true
                isChecked = status == current
                setOnClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        repo.setStatus(id, status)
                        _binding?.spinnerStatus?.setSelection(status.ordinal)
                    }
                }
            }
            binding.quickStatusChips.addView(chip)
        }
    }

    private fun updateStar(starred: Boolean) {
        val res = if (starred) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off
        binding.btnStar.setImageResource(res)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
