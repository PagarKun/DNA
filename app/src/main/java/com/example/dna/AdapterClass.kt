package com.example.dna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class AdapterClass(
    private val dataList: List<DataClass>,
    private val onItemClick: (DataClass) -> Unit
) : RecyclerView.Adapter<AdapterClass.ViewHolderClass>() {

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val card: MaterialCardView = itemView as MaterialCardView
        val image: ImageView = itemView.findViewById(R.id.profileImage)
        val nama: TextView = itemView.findViewById(R.id.namakaryawan2)
        val keahlian: TextView = itemView.findViewById(R.id.keahlian)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_karyawan, parent, false)
        return ViewHolderClass(view)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val item = dataList[position]

        holder.image.setImageResource(item.dataImage)
        holder.nama.text = item.dataNama
        holder.keahlian.text = item.dataKeahlian

        holder.card.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = dataList.size
}

class BebanAdapter(private val bebanList: List<BebanClass>) :
    RecyclerView.Adapter<BebanAdapter.ViewHolderClasskinerja>() {

    class ViewHolderClasskinerja(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rvTitle: TextView = itemView.findViewById(R.id.title_beban)
        val rvAngka: TextView = itemView.findViewById(R.id.angka_beban)


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderClasskinerja {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_beban_kerja, parent, false)
        return ViewHolderClasskinerja(view)
    }

    override fun onBindViewHolder(holder: ViewHolderClasskinerja, position: Int) {
        val currentItem = bebanList[position]
        holder.rvTitle.text = currentItem.dataTitle
        holder.rvAngka.text = currentItem.dataAngka

//        BG COLOR Item Beban card
        val card = holder.itemView as MaterialCardView

        val bgcolors = listOf(
            ContextCompat.getColor(card.context, R.color.white),
            ContextCompat.getColor(card.context, R.color.peach),
            ContextCompat.getColor(card.context, R.color.hijau),
            ContextCompat.getColor(card.context, R.color.kuning),

            )
        card.setCardBackgroundColor(bgcolors[position % bgcolors.size])

        val strokeColor = listOf(
            ContextCompat.getColor(card.context, R.color.blueterang),
            ContextCompat.getColor(card.context, R.color.peach2),
            ContextCompat.getColor(card.context, R.color.hijau2),
            ContextCompat.getColor(card.context, R.color.kuning2),
        )
        card.strokeColor = strokeColor[position % strokeColor.size]

        val strokeWidths = listOf(2, 2, 2, 2)

        card.strokeWidth = strokeWidths[position % strokeWidths.size]


    }

    override fun getItemCount(): Int = bebanList.size
}


