package com.practicum.taskmanager.ui

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.practicum.taskmanager.R
import com.practicum.taskmanager.databinding.ItemCalendarDayBinding
import com.practicum.taskmanager.util.DateUtils
import java.util.Calendar

class CalendarDayAdapter(
    private var days: List<DateUtils.CalendarDay> = emptyList(),
    private var selected: Calendar = Calendar.getInstance(),
    private var datesWithTasks: Set<String> = emptySet(),
    private val onDayClick: (Calendar) -> Unit,
) : RecyclerView.Adapter<CalendarDayAdapter.ViewHolder>() {

    fun submit(
        days: List<DateUtils.CalendarDay>,
        selected: Calendar,
        datesWithTasks: Set<String>,
    ) {
        this.days = days
        this.selected = selected
        this.datesWithTasks = datesWithTasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCalendarDayBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount(): Int = days.size

    inner class ViewHolder(private val binding: ItemCalendarDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(day: DateUtils.CalendarDay) {
            val ctx = binding.root.context
            val iso = DateUtils.toIsoDate(day.date)
            val hasTasks = datesWithTasks.contains(iso)
            val isSelected = DateUtils.isSameDay(day.date, selected)
            val isToday = DateUtils.isToday(day.date)

            binding.textDay.text = day.date.get(Calendar.DAY_OF_MONTH).toString()

            val bg = when {
                isSelected -> R.drawable.bg_pill_primary
                else -> android.R.color.transparent
            }
            binding.textDay.setBackgroundResource(bg)

            val textColor = when {
                isSelected -> R.color.white
                !day.inMonth -> R.color.primary_muted
                isToday -> R.color.primary
                else -> R.color.black
            }
            binding.textDay.setTextColor(ContextCompat.getColor(ctx, textColor))
            binding.textDay.setTypeface(
                null,
                if (isSelected || isToday) Typeface.BOLD else Typeface.NORMAL,
            )

            binding.root.setOnClickListener { onDayClick(day.date) }
        }
    }
}
