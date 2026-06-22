package com.practicum.taskmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.taskmanager.R
import com.practicum.taskmanager.TaskManagerApp
import com.practicum.taskmanager.databinding.FragmentScheduleBinding
import com.practicum.taskmanager.util.DateUtils
import java.util.Calendar

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var calendarAdapter: CalendarDayAdapter
    private lateinit var tasksAdapter: ScheduleTaskAdapter

    private var viewDate: Calendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1)
    }
    private var selected: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repo = (requireActivity().application as TaskManagerApp).repository

        calendarAdapter = CalendarDayAdapter { day ->
            selected = day.clone() as Calendar
            renderCalendar(repo)
            renderTasks(repo)
        }
        binding.recyclerCalendar.layoutManager = GridLayoutManager(requireContext(), 7)
        binding.recyclerCalendar.adapter = calendarAdapter

        tasksAdapter = ScheduleTaskAdapter { task ->
            findNavController().navigate(
                R.id.taskDetailFragment,
                Bundle().apply { putString("taskId", task.id) },
            )
        }
        binding.recyclerDayTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDayTasks.adapter = tasksAdapter

        binding.btnPrevMonth.setOnClickListener {
            viewDate.add(Calendar.MONTH, -1)
            renderCalendar(repo)
        }
        binding.btnNextMonth.setOnClickListener {
            viewDate.add(Calendar.MONTH, 1)
            renderCalendar(repo)
        }

        renderCalendar(repo)
        renderTasks(repo)
    }

    private fun renderCalendar(repo: com.practicum.taskmanager.data.TaskRepository) {
        val year = viewDate.get(Calendar.YEAR)
        val month = viewDate.get(Calendar.MONTH)
        binding.textMonth.text = DateUtils.monthLabel(year, month)

        val taskDates = repo.getTasks().map { it.dueDate }.toSet()
        val days = DateUtils.getCalendarDays(year, month)
        calendarAdapter.submit(days, selected, taskDates)
    }

    private fun renderTasks(repo: com.practicum.taskmanager.data.TaskRepository) {
        binding.textSelectedDay.text = DateUtils.formatSelectedDayRu(selected)
        val iso = DateUtils.toIsoDate(selected)
        val dayTasks = repo.getTasks().filter { it.dueDate == iso }
        tasksAdapter.submitList(dayTasks)
        binding.textEmpty.visibility = if (dayTasks.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        val repo = (requireActivity().application as TaskManagerApp).repository
        renderCalendar(repo)
        renderTasks(repo)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
