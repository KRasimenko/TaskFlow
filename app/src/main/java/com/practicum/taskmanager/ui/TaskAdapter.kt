package com.practicum.taskmanager.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.practicum.taskmanager.R
import com.practicum.taskmanager.data.Task
import com.practicum.taskmanager.databinding.ItemTaskBinding
import com.practicum.taskmanager.util.DateUtils
import com.practicum.taskmanager.util.TaskUiHelper

class TaskAdapter(
    private var items: List<Task> = emptyList(),
    private val onTaskClick: (Task) -> Unit,
    private val onStarClick: (Task) -> Unit,
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    fun submitList(list: List<Task>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            val ctx = binding.root.context
            binding.textCategory.text = task.category
            binding.textTitle.text = task.title
            binding.textDescription.text = task.description
            binding.textPriority.text = TaskUiHelper.priorityLabel(ctx, task.priority)
            binding.textStatus.text = TaskUiHelper.statusLabel(ctx, task.status)
            binding.textDate.text = DateUtils.formatDateRu(task.dueDate)

            val starRes = if (task.starred) {
                android.R.drawable.btn_star_big_on
            } else {
                android.R.drawable.btn_star_big_off
            }
            binding.btnStar.setImageResource(starRes)
            binding.btnStar.setColorFilter(
                ContextCompat.getColor(
                    ctx,
                    if (task.starred) R.color.primary else R.color.primary_muted,
                ),
            )

            if (task.priority == com.practicum.taskmanager.data.TaskPriority.HIGH) {
                binding.textPriority.setBackgroundResource(R.drawable.bg_pill_primary)
                binding.textPriority.setTextColor(ContextCompat.getColor(ctx, R.color.white))
            } else {
                binding.textPriority.setBackgroundResource(R.drawable.bg_pill_white)
                binding.textPriority.setTextColor(ContextCompat.getColor(ctx, R.color.primary))
            }

            binding.root.alpha = if (task.status == com.practicum.taskmanager.data.TaskStatus.DONE) 0.75f else 1f
            binding.root.setOnClickListener { onTaskClick(task) }
            binding.btnStar.setOnClickListener { onStarClick(task) }
        }
    }
}
