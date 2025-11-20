package com.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dna.R

class KaryawanAdapter(
    private val listKaryawan: List<Karyawan>
) : RecyclerView.Adapter<KaryawanAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foto = itemView.findViewById<ImageView>(R.id.profileImage12)
        val nama = itemView.findViewById<TextView>(R.id.nama_kinerja)
        val keahlian = itemView.findViewById<TextView>(R.id.keahlian_kinerja)
        val departemen = itemView.findViewById<TextView>(R.id.departemen)

        val jamKerja = itemView.findViewById<TextView>(R.id.jamKerja)
        val jumlahTask = itemView.findViewById<TextView>(R.id.jumlahTask)
        val periode = itemView.findViewById<TextView>(R.id.periode)

        val childRecycler = itemView.findViewById<RecyclerView>(R.id.childTaskRecycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_karyawanparent, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listKaryawan[position]

        holder.foto.setImageResource(data.foto)
        holder.nama.text = data.nama
        holder.keahlian.text = data.keahlian
        holder.departemen.text = data.departemen

        holder.jamKerja.text = data.jamKerja
        holder.jumlahTask.text = "${data.jumlahTask} Task"
        holder.periode.text = data.periode

        // Set RecyclerView Anak
        holder.childRecycler.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.childRecycler.adapter = TaskAdapter(data.taskList)
    }

    override fun getItemCount(): Int = listKaryawan.size
}