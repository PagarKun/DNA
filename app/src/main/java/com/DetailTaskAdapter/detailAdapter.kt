package com.DetailTaskAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Adapter.detailBeban
import com.example.dna.R
import com.Adapter.detailBebanAdapter
import java.util.zip.Inflater
import com.RangeAPI.Task


class detailAdapter(private val datadata: List<detailTaskModel>)
    : RecyclerView.Adapter<detailAdapter.detailTaskViewHolder>(){



    inner class detailTaskViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val judul = itemView.findViewById<TextView>(R.id.tv_title1)
        val jam = itemView.findViewById<TextView>(R.id.tv_time)
        val desc = itemView.findViewById<TextView>(R.id.tv_desc)
        val tanggal = itemView.findViewById<TextView>(R.id.tv_date)
        val project = itemView.findViewById<TextView>(R.id.tv_project)
        val progress = itemView.findViewById<TextView>(R.id.tv_status)
        val level = itemView.findViewById<TextView>(R.id.tv_priority)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): detailAdapter.detailTaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail_task,parent,false)
        return detailTaskViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: detailAdapter.detailTaskViewHolder,
        position: Int
    ) {
        val data = datadata[position]

        holder.judul.text = data.judul
        holder.desc.text = data.desc
        holder.jam.text = data.jam
        holder.tanggal.text = data.tanggal
        holder.project.text = data.project
        holder.progress.text = data.progress
        holder.level.text = data.level
    }


    override fun getItemCount(): Int = datadata.size



}