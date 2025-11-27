package com.Nyoba

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dna.R

class TaskAdapter(private val tasks: List<Task>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTaskTitle: TextView = view.findViewById(R.id.tvTaskTitle)
        val tvTaskStatus: TextView = view.findViewById(R.id.tvTaskStatus)
        val tvTaskDeadline: TextView = view.findViewById(R.id.tvTaskDeadline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task_nyoba, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.tvTaskTitle.text = task.title
        holder.tvTaskStatus.text = task.status
        holder.tvTaskDeadline.text = task.deadline
    }

    override fun getItemCount() = tasks.size
}