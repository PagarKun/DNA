package com.example.dna

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.MemberAPI.MemberRequest
import com.google.android.material.card.MaterialCardView
import com.bumptech.glide.Glide
import com.MemberAPI.RetrofitClient
const val BASE_URL = "https://go-kinerja-backend-production.up.railway.app/api/v1/clickup/members"


class AdapterClass(
    private val dataList: MutableList<MemberRequest>,
    private val onItemClick: (MemberRequest) -> Unit
) : RecyclerView.Adapter<AdapterClass.ViewHolderClass>() {

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val card: MaterialCardView = itemView.findViewById(R.id.card_karyawan)
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
        Log.d ("BIND_ITEM", item.name)

        holder.nama.text = item.name
        holder.keahlian.text = item.role
        Glide.with(holder.image.context)
            .load(BASE_URL + item.photo?.removePrefix("/"))
            .error(R.drawable.apasih)
            .into(holder.image)

        holder.card.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = dataList.size

    fun filterList(filteredList:List<MemberRequest>) {
        dataList.clear()
        dataList.addAll(filteredList)
        notifyDataSetChanged()


    }
}



// BEBAN KERJA

class BebanAdapter(private val bebanList: MutableList<BebanClass>) :
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

    fun updateData(newList:List<BebanClass>) {
        bebanList.clear()
        bebanList.addAll(newList)
        notifyDataSetChanged()
    }
}





