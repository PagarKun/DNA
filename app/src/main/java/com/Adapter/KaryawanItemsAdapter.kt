package com.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.Filter
import android.widget.Filter.FilterResults
import android.widget.Filterable
import android.widget.TextView
import com.MemberAPI.PosisiItem
import com.example.dna.R

class KaryawanItemsAdapter (
    context: Context,
    var items: MutableList<KaryawanItems>
) : ArrayAdapter<KaryawanItems>(context,0, items) {

    private var filteredItems: List<KaryawanItems> = items
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int = filteredItems.size

    override fun getItem(position: Int): KaryawanItems = filteredItems[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.dropdown_nama_karyawan, parent, false)
            viewHolder = ViewHolder(
                view.findViewById(R.id.list_namakaryawan),
                view.findViewById(R.id.item_checkbox)
            )
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val item = getItem(position)
        viewHolder.textView?.text = item.namas
        viewHolder.checkBox?.isChecked = item.isChecked

        return view
    }

    private class ViewHolder(val textView: TextView?, val checkBox: CheckBox?)

    private val originalItems: List<KaryawanItems> = ArrayList(items)

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val Results = FilterResults()
                val queryString = constraint?.toString()?.toLowerCase()

                val filteredList = if (queryString.isNullOrEmpty()) {
                    originalItems
                } else {
                    originalItems.filter { it.namas.toLowerCase().contains(queryString)}
                }

                Results.values = filteredList
                Results.count= filteredList.size
                return Results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                items.clear()

                if (results?.values != null) {
                    items.addAll(results.values as List<KaryawanItems>)
                }
                notifyDataSetChanged()
            }
        }
    }
}
