package com.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dna.R

class KaryawanAdapter(
    private val listKaryawan: MutableList<Karyawan>,
    private val onDetailButtonClicked: (Karyawan) -> Unit
) : RecyclerView.Adapter<KaryawanAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // View Utama
        val foto: ImageView = itemView.findViewById(R.id.profileImage12)
        val nama: TextView = itemView.findViewById(R.id.nama_kinerja)
        val keahlian: TextView = itemView.findViewById(R.id.keahlian_kinerja)
        val btnTaskDetail: LinearLayout = itemView.findViewById(R.id.taskdetailBtn)

        // View untuk Expandable
        val headerBeban: LinearLayout = itemView.findViewById(R.id.headerBeban)
        val expandableBeban: LinearLayout = itemView.findViewById(R.id.expandableBeban) // Pastikan ID ini benar
        val arrowBeban: ImageView = itemView.findViewById(R.id.arrowBeban)

        // View DI DALAM Expandable Layout
        val detailJamKerja: TextView = itemView.findViewById(R.id.detail_jam_kerja)

        val detailJamKerjaseharusnya: TextView = itemView.findViewById(R.id.detail_jam_kerja_seharusnya)
        val detailPerforma: TextView = itemView.findViewById(R.id.detail_performa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_karyawanparent, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataKaryawan = listKaryawan[position]

        // --- Set Data Utama ---
        holder.foto.setImageResource(dataKaryawan.foto)
        holder.nama.text = dataKaryawan.nama
        holder.keahlian.text = dataKaryawan.keahlian

        // --- Set Data DI DALAM Expandable Layout ---
        // Anda perlu menambahkan data ini ke data class Karyawan atau menghitungnya di sini
        holder.detailJamKerja.text = "${dataKaryawan.totalSpentHoursFromApi} Jam dari ${dataKaryawan.totalTaskFromApi} task"
        holder.detailJamKerjaseharusnya.text = "${dataKaryawan.totalActualHoursFormApi}Jam dari ${dataKaryawan.totalTaskFromApi} task"
        holder.detailPerforma.text = "0.9% dari standar" // Contoh, data ini perlu Anda sediakan

        // --- Logika Expand / Collapse (SAMA SEPERTI SEBELUMNYA) ---
        if (dataKaryawan.isBebanExpanded) {
            holder.expandableBeban.visibility = View.VISIBLE
            holder.arrowBeban.rotation = 180f
        } else {
            holder.expandableBeban.visibility = View.GONE
            holder.arrowBeban.rotation = 0f
        }

        holder.headerBeban.setOnClickListener {
            dataKaryawan.isBebanExpanded = !dataKaryawan.isBebanExpanded
            notifyItemChanged(position)
        }

        holder.btnTaskDetail.setOnClickListener {
            onDetailButtonClicked(dataKaryawan)
        }
    }

    override fun getItemCount(): Int = listKaryawan.size

    fun updateList(newList: List<Karyawan>) {
        listKaryawan.clear()
        listKaryawan.addAll(newList)
        notifyDataSetChanged()
    }
}
