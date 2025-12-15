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

// Adapter list detail beban kerja.
class detailBebanAdapter(
    private var allData: List<detailBeban>,
    private val onItemClick: (detailBeban) -> Unit,

    private var bgcolors: List<Int>? = null
) : RecyclerView.Adapter<detailBebanAdapter.detailViewHolder>() {

    // Buat ViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): detailViewHolder {
        if (bgcolors == null) {
            bgcolors = listOf(
                ContextCompat.getColor(parent.context, R.color.blue3),
                ContextCompat.getColor(parent.context, R.color.gray)
            )

        }

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail_beban, parent, false)
        return detailViewHolder(view)
    }

    // Data ke ViewHolder.
    override fun onBindViewHolder(holder: detailViewHolder, position: Int) {
        val data = allData[position]

        // Set data.
        holder.judul.text = data.judul
        holder.detailjam.text = data.totaljam
        holder.detailtask.text = data.totaltask
        holder.detailgambar.setImageResource(data.gambar)

        // Set warna card.
        val card = holder.card
        bgcolors?.let {colors ->
            card.setCardBackgroundColor(colors[position % colors.size])
        }


        // Set listener.
        holder.itemView.setOnClickListener {
            onItemClick(data)
        }
    }

    // Jumlah item.
    override fun getItemCount(): Int = allData.size

    fun updateData(newData: List<detailBeban>) {
        allData = newData
        notifyDataSetChanged()
    }



    // Referensi view.
    inner class detailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val judul: TextView = itemView.findViewById(R.id.titlebeban)
        val detailjam: TextView = itemView.findViewById(R.id.total_jam_kerja_aktif)
        val detailtask: TextView = itemView.findViewById(R.id.total_task_kerja_aktif)
        val detailgambar: ImageView = itemView.findViewById(R.id.gambardetail)
        val card: MaterialCardView = itemView.findViewById(R.id.card_beban)
    }
}
