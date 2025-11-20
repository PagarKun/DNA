package com.fragment.dna

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dna.AdapterClass
import com.example.dna.DataClass
import com.example.dna.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class DashBoardFragment : Fragment() {

    private lateinit var recycleView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_dash_board, container, false)

        // init recyclerview
        recycleView = view.findViewById(R.id.recycleViews)
        recycleView.layoutManager = LinearLayoutManager(requireContext())
        recycleView.setHasFixedSize(true)

        // isi data
        val imageList = arrayOf(
            R.drawable.apasih,
            R.drawable.apasih,
            R.drawable.apasih,
            R.drawable.apasih
        )

        val namaList = arrayOf(
            "Rizqi",
            "Rizqi2",
            "Rizqi3",
            "Rizqi4"
        )

        val keahlianList = arrayOf(
            "Programmer",
            "Programmer2",
            "Programmer3",
            "Programmer4"
        )

        // list data
        dataList = arrayListOf()
        for (i in imageList.indices) {
            dataList.add(DataClass(imageList[i], namaList[i], keahlianList[i]))
        }

        // adapter on click
        val adapter = AdapterClass(dataList) { item ->
            showBottomSheetDetail(item)
        }

        recycleView.adapter = adapter

        return view
    }

    fun showBottomSheetDetail(item: DataClass) {
        val bottomSheet = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.popup_detail_karyawan, null)

        val img = view.findViewById<ImageView>(R.id.profileDetail)
        val nama = view.findViewById<TextView>(R.id.namaDetail)
        val keahlian = view.findViewById<TextView>(R.id.keahlianDetail)

        img.setImageResource(item.dataImage)
        nama.text = item.dataNama
        keahlian.text = item.dataKeahlian

        bottomSheet.setContentView(view)
        bottomSheet.behavior.peekHeight = 900
        bottomSheet.show()
    }
}