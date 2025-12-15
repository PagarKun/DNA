package com.DetailTaskAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.RangeAPI.Assignee
import com.example.dna.R
import com.RangeAPI.Task
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
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
        val time_effeciency: TextView = itemView.findViewById(R.id.tv_Ketepatan_pengerjaan_tugas)
        val remaining_time: TextView = itemView.findViewById(R.id.tv_remaining_time)

        val LL_ketepatan: LinearLayout = itemView.findViewById(R.id.LL_Ketepatan_pengerjaan_tugas)
        val card_time_persentage: MaterialCardView = itemView.findViewById(R.id.card_time_persentage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): detailTaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail_task, parent, false)
        return detailTaskViewHolder(view)
    }


    override fun onBindViewHolder(holder: detailTaskViewHolder, position: Int) {
        val task = taskList[position]

        val percentageValue = task.timeEfficiencyPercentage ?: 0.0F
        val backgroundColorResource : Int
        val textColorAndImageResource: Int
        val strokeColor: Int





        if (percentageValue < 80.0F) {
            backgroundColorResource = R.color.red3
            textColorAndImageResource = R.color.red
            strokeColor = R.color.red4

        } else if (percentageValue == 100F) {
            backgroundColorResource = R.color.abuabu3
            textColorAndImageResource = R.color.black
            strokeColor = R.color.abuabu2
        }

        else {
            backgroundColorResource = R.color.hijau3
            textColorAndImageResource = R.color.hijaugelap
            strokeColor = R.color.hijaustroke
        }


        val backgroundColor = ContextCompat.getColor(holder.itemView.context, backgroundColorResource)
        val time_effeciency = ContextCompat.getColor(holder.itemView.context, textColorAndImageResource)
        val StrokeColor = ContextCompat.getColor(holder.itemView.context, strokeColor)

        holder.LL_ketepatan.setBackgroundColor(backgroundColor)
        holder.time_effeciency.setTextColor(time_effeciency)
        holder.card_time_persentage.setStrokeColor(StrokeColor)


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
        holder.priorityChip.text = task.priority ?: "No Priority"
        holder.time_effeciency.text = "${task.timeEfficiencyPercentage ?: 0}%"
        holder.remaining_time.text = task.remainingTime ?: "Unknown"
    }


    override fun getItemCount(): Int = taskList.size


    fun updateTasks(newTasks: List<Task>) {
        this.taskList = newTasks
        notifyDataSetChanged()
    }
}
