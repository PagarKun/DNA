package com.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.DetailTaskAdapter.detailAdapter
import com.DetailTaskAdapter.detailTaskModel
import com.example.dna.R


class TaskAdapter(private val taskList: List<Task>)
    : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaTask = itemView.findViewById<TextView>(R.id.taskTitle)
        val rvTask : RecyclerView = itemView.findViewById(R.id.rvTask)
        val layoutExpandable: LinearLayout = itemView.findViewById(R.id.expandableTaskdetail)
        val isExpandedArrow : ConstraintLayout = itemView.findViewById(R.id.headerDetailTask)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val data = taskList[position]

        holder.rvTask.layoutManager = LinearLayoutManager(holder.itemView.context)

        val datatasks =
            detailTaskModel (
            judul = "Develop",
            judul2 = "Landing Page",
            jam = "32 Jam",
            tanggal = "12/12/2001212",
            project = "Clikclok",
            progress = "Selesai",
            level = "Highhhh",
                isExpanded = false
        )
        val datatask2 =
            detailTaskModel (
            judul = "asdasdasdasd",
            judul2 = "2222asdasdadasd22",
            jam = "32 Jam",
            tanggal = "12/12/2001212",
            project = "Clikclok",
            progress = "Selesai",
            level = "Highhhh",
                isExpanded = false
        )

        val allData = listOf(datatasks,datatask2)

        holder.rvTask.layoutManager = LinearLayoutManager (holder.itemView.context)
        holder.rvTask.adapter = detailAdapter(allData)



        holder.layoutExpandable.visibility = if (data.isExpanded) View.VISIBLE else View.GONE

        // Click listener for expand/collapse
        holder.isExpandedArrow.setOnClickListener {
            data.isExpanded = !data.isExpanded
            notifyItemChanged(position)
        }

    }

    override fun getItemCount(): Int = taskList.size
}