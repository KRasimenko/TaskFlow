package com.practicum.taskmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.taskmanager.R
import com.practicum.taskmanager.TaskManagerApp
import com.practicum.taskmanager.data.AppNotification
import com.practicum.taskmanager.databinding.ItemNotificationBinding

class NotificationsFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var emptyView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_notifications, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view.findViewById(R.id.recycler_notifications)
        emptyView = view.findViewById(R.id.text_empty)

        val repo = (requireActivity().application as TaskManagerApp).repository
        val items = repo.getNotifications()

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = NotificationAdapter(items) { notification ->
            findNavController().navigate(
                R.id.taskDetailFragment,
                Bundle().apply { putString("taskId", notification.taskId) },
            )
        }

        emptyView.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
    }

    private class NotificationAdapter(
        private val items: List<AppNotification>,
        private val onClick: (AppNotification) -> Unit,
    ) : RecyclerView.Adapter<NotificationAdapter.Holder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val binding = ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return Holder(binding)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size

        inner class Holder(private val binding: ItemNotificationBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(item: AppNotification) {
                binding.textTitle.text = item.title
                binding.textBody.text = item.body
                binding.textDate.text = item.date
                binding.root.setOnClickListener { onClick(item) }
            }
        }
    }
}
