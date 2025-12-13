package com.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Adapter.Task
import com.DetailTaskAdapter.detailTaskModel
import com.example.dna.R
import com.DetailTaskAdapter.detailAdapter
import com.RangeAPI.Assignee


class TaskAdapter(private val taskList: List<Task>, private val assigneeList: List<AssigneeClass>)
    : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaTask = itemView.findViewById<TextView>(R.id.taskTitle)
        val rvTask : RecyclerView = itemView.findViewById(R.id.rvTask)
        val layoutExpandable: LinearLayout = itemView.findViewById(R.id.expandableTaskdetail)
        val arrowdetailTask : ImageView = itemView.findViewById(R.id.arrowdetailTask)
        val isExpandedArrow : ConstraintLayout = itemView.findViewById(R.id.headerDetailTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = taskList[position]


        holder.namaTask.text = data.judul

        val uniqueDetailData = data.detailTaskList

        holder.rvTask.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.rvTask.adapter = detailAdapter(uniqueDetailData)


        holder.layoutExpandable.visibility = if (data.isExpanded) View.VISIBLE else View.GONE
        holder.arrowdetailTask.rotation = if (data.isExpanded) 180f else 0f
        holder.isExpandedArrow.setOnClickListener {
            data.isExpanded = !data.isExpanded
            notifyItemChanged(position)
        }
    }



    override fun getItemCount(): Int = taskList.size
}