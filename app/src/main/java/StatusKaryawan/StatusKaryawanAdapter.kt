package StatusKaryawan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Filter
import android.widget.TextView
import com.example.dna.R

class StatusKaryawanAdapter (
    context: Context,
    items1: Int,
    var items: MutableList<StatusKaryawanModel>
) : ArrayAdapter<StatusKaryawanModel>(context,0, items) {

    private var filteredItems: List<StatusKaryawanModel> = items
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int = filteredItems.size

    override fun getItem(position: Int): StatusKaryawanModel = filteredItems[position]

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
        viewHolder.textView?.text = item.status
        viewHolder.checkBox?.isChecked = item.isChecked

        return view
    }

    private class ViewHolder(val textView: TextView?, val checkBox: CheckBox?)

    private val originalItems: List<StatusKaryawanModel> = ArrayList(items)

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val Results = FilterResults()
                val queryString = constraint?.toString()?.toLowerCase()

                val filteredList = if (queryString.isNullOrEmpty()) {
                    originalItems
                } else {
                    originalItems.filter { it.status.toLowerCase().contains(queryString)}
                }

                Results.values = filteredList
                Results.count= filteredList.size
                return Results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                items.clear()

                if (results?.values != null) {
                    items.addAll(results.values as List<StatusKaryawanModel>)
                }
                notifyDataSetChanged()
            }
        }
    }
}
