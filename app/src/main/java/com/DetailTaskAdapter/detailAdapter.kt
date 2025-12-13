package com.DetailTaskAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.RangeAPI.Assignee
import com.example.dna.R
import com.RangeAPI.Task
import com.google.android.material.appbar.MaterialToolbar
import org.w3c.dom.Text

class detailAdapter(

    private var taskList: List<Task>
) : RecyclerView.Adapter<detailAdapter.detailTaskViewHolder>() {
    inner class detailTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.tv_title1)
        val project: TextView = itemView.findViewById(R.id.tv_project)
        val desc: TextView = itemView.findViewById(R.id.tv_desc)
        val jam: TextView = itemView.findViewById(R.id.tv_time)
        val tanggalmulai: TextView = itemView.findViewById(R.id.tv_date)
        val deadline : TextView = itemView.findViewById(R.id.tv_date_deadline)
        val tanggalselesai: TextView = itemView.findViewById(R.id.tv_date_selesai)
        val statusChip: TextView = itemView.findViewById(R.id.tv_status)
        val priorityChip: TextView = itemView.findViewById(R.id.tv_priority)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): detailTaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail_task, parent, false)
        return detailTaskViewHolder(view)
    }


    override fun onBindViewHolder(holder: detailTaskViewHolder, position: Int) {
        val task = taskList[position]

        holder.title.text = task.name ?: "Tanpa Judul"

        holder.project.text = task.projectName ?: "Tanpa Project"

        if (task.deskripsi.isNullOrBlank()) {
            holder.desc.visibility = View.GONE
        } else {
            holder.desc.visibility = View.VISIBLE
            holder.desc.text = task.deskripsi
        }



        holder.jam.text = "${task.timeEstimateHours ?: 0} jam"
        holder.tanggalmulai.text = " ${task.startDate ?: "-"}"
        holder.deadline.text = " ${task.dueDate ?: "-"}"
        holder.tanggalselesai.text = " ${task.dateDone ?: "-"}"
        holder.statusChip.text = task.statusName ?: "Unknown"
        holder.priorityChip.text = task.priority ?: "Unknown"
    }


    override fun getItemCount(): Int = taskList.size


    fun updateTasks(newTasks: List<Task>) {
        this.taskList = newTasks
        notifyDataSetChanged()
    }
}
