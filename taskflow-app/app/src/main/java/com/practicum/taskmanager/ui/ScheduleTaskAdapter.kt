package com.practicum.taskmanager.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.practicum.taskmanager.R
import com.practicum.taskmanager.data.Task
import com.practicum.taskmanager.data.TaskPriority
import com.practicum.taskmanager.databinding.ItemScheduleTaskBinding

class ScheduleTaskAdapter(
    private var items: List<Task> = emptyList(),
    private val onClick: (Task) -> Unit,
) : RecyclerView.Adapter<ScheduleTaskAdapter.ViewHolder>() {

    fun submitList(list: List<Task>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemScheduleTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemScheduleTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.textTitle.text = task.title
            binding.textCategory.text = task.category
            val color = when (task.priority) {
                TaskPriority.HIGH -> R.color.primary
                TaskPriority.MEDIUM -> R.color.primary_muted
                TaskPriority.LOW -> android.R.color.darker_gray
            }
            binding.dotPriority.setBackgroundColor(
                ContextCompat.getColor(binding.root.context, color),
            )
            binding.root.setOnClickListener { onClick(task) }
        }
    }
}
