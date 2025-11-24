package com.example.dna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.Adapter.Karyawan

class KaryawanAdapter(private val list: List<Karyawan>) :
    RecyclerView.Adapter<KaryawanAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nama = itemView.findViewById<TextView>(R.id.namakaryawan2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_karyawan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.nama.text = data.nama
    }

    override fun getItemCount(): Int = list.size
}