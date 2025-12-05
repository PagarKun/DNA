package com.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.MemberAPI.MemberRequest
import com.example.dna.R
import com.google.android.material.card.MaterialCardView

class KaryawanAdapter(
    private val listKaryawan: MutableList<Karyawan> // <-- HANYA SATU PARAMETER
) : RecyclerView.Adapter<KaryawanAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foto = itemView.findViewById<ImageView>(R.id.profileImage12)
        val nama = itemView.findViewById<TextView>(R.id.nama_kinerja)
        val keahlian = itemView.findViewById<TextView>(R.id.keahlian_kinerja)
        val departemen = itemView.findViewById<TextView>(R.id.departemen)


        val ivExpandArrow: LinearLayout = itemView.findViewById(R.id.headerTask)
        val ivExpandArrow_beban: LinearLayout = itemView.findViewById(R.id.headerBeban)
        val ivExpandArrow_task_image: ImageView = itemView.findViewById(R.id.arrowTask)
        val ivExpandArrow_beban_image: ImageView = itemView.findViewById(R.id.arrowBeban)
        val layoutExpandable: LinearLayout = itemView.findViewById(R.id.expandableTask)
        val layoutExpandable_beban: LinearLayout = itemView.findViewById(R.id.expandableBeban)


        val childRecycler = itemView.findViewById<RecyclerView>(R.id.rvTasks)
        val rvBeban : RecyclerView = itemView.findViewById(R.id.rvBeban)


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



        // Set RecyclerView Anak
        holder.childRecycler.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.childRecycler.adapter = TaskAdapter(data.taskList)
        holder.rvBeban.layoutManager = LinearLayoutManager(holder.itemView.context)

        val databebandata =
            detailBeban (
                judul = "Total Beban kerja (Aktif)",
                totaltask = "72 jam",
                totaljam = "2 task",
                gambar = R.drawable.jam
            )


        val peforma =
            detailBeban (
                judul = "Peforma",
                totaltask = "Standar",
                totaljam = "40%",
                gambar = R.drawable.ic_task
            )




        val allData = listOf(databebandata,peforma)

        holder.rvBeban.layoutManager = LinearLayoutManager (holder.itemView.context)
        holder.rvBeban.adapter = detailBebanAdapter(allData)


        // Show/hide expandable section Task   // Rotate arrow
        holder.layoutExpandable.visibility = if (data.isExpanded) View.VISIBLE else View.GONE
        holder.ivExpandArrow_task_image.rotation = if (data.isExpanded) 180f else 0f


        // Show/hide expandable section Beban   // Rotate arrow
        holder.layoutExpandable_beban.visibility = if (data.isBebanExpanded) View.VISIBLE else View.GONE
        holder.ivExpandArrow_beban_image.rotation = if (data.isBebanExpanded) 180f else 0f


        // Click listener for expand/collapse
        holder.ivExpandArrow.setOnClickListener {
            data.isExpanded = !data.isExpanded
            notifyItemChanged(position)
        }
        holder.ivExpandArrow_beban.setOnClickListener {
            data.isBebanExpanded = !data.isBebanExpanded
            notifyItemChanged(position)
        }

    }

    override fun getItemCount(): Int = listKaryawan.size

    fun updateList(newList: List<Karyawan>) {
        listKaryawan.clear()
        listKaryawan.addAll(newList)
        notifyDataSetChanged()
    }
}