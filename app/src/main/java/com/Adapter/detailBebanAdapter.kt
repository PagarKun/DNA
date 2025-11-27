package com.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dna.R
import com.google.android.material.card.MaterialCardView

class detailBebanAdapter(
    private val allData: List<detailBeban>,
) : RecyclerView.Adapter<detailBebanAdapter.detailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): detailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail_beban, parent, false)
        return detailViewHolder(view)
    }

    override fun onBindViewHolder(holder: detailViewHolder, position: Int) {
        val data = allData[position]

        holder.judul
        holder.detailjam.text = data.totaljam
        holder.detailtask.text = data.totaltask
        holder.detailgambar.setImageResource(data.gambar)

        val card = holder.itemView as MaterialCardView

        val bgcolors = listOf(
            ContextCompat.getColor(card.context, R.color.blue3),
            ContextCompat.getColor(card.context, R.color.gray),


            )
        card.setCardBackgroundColor(bgcolors[position % bgcolors.size])
    }

    override fun getItemCount(): Int = allData.size

    inner class detailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val judul: TextView = itemView.findViewById(R.id.titlebeban)
        val detailjam: TextView = itemView.findViewById(R.id.total_jam_kerja_aktif)
        val detailtask: TextView = itemView.findViewById(R.id.total_task_kerja_aktif)
        val detailgambar: ImageView = itemView.findViewById(R.id.gambardetail)
        val card: MaterialCardView = itemView.findViewById(R.id.card_beban)

    }
}