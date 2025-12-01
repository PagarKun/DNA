package com.example.dna

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.MemberAPI.PosisiItem

class PosisiAdapter(
    private val context: Context,
    items1: Int,
    var items: List<PosisiItem>
) : BaseAdapter(), Filterable {

    private var filteredItems: List<PosisiItem> = items
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int = filteredItems.size

    override fun getItem(position: Int): PosisiItem = filteredItems[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.dropdown_posisi_karyawan, parent, false)
            viewHolder = ViewHolder(
                view.findViewById(R.id.list_Posisi),
                view.findViewById(R.id.item_checkbox)
            )
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val item = getItem(position)
        viewHolder.textView?.text = item.nama
        viewHolder.checkBox?.isChecked = item.isChecked

        return view
    }

    private class ViewHolder(val textView: TextView?, val checkBox: CheckBox?)

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val queryString = constraint?.toString()?.toLowerCase()

                filterResults.values = if (queryString.isNullOrEmpty()) {
                    items
                } else {
                    items.filter { it.nama.toLowerCase().contains(queryString) }
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredItems = results?.values as? List<PosisiItem> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}
