package com.fragment.dna

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dna.AdapterClass
import com.example.dna.DataClass
import com.example.dna.R
import kotlin.text.get


class DashBoardFragment : Fragment() {

    private lateinit var recycleView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // inflate dulu
        val view = inflater.inflate(R.layout.fragment_dash_board, container, false)

        // inisialisasi RecyclerView
        recycleView = view.findViewById(R.id.recycleViews)
        recycleView.layoutManager = LinearLayoutManager(requireContext())
        recycleView.setHasFixedSize(true)

        // data
        val imageList = arrayOf(
            R.drawable.apasih,
            R.drawable.apasih,
            R.drawable.apasih,
            R.drawable.apasih
        )

        val namaList = arrayOf(
            "Rizqi",
            "Rizqi",
            "Rizqi",
            "Rizqi"
        )

        val keahlianList = arrayOf(
            "Programmer",
            "Programmer",
            "Programmer",
            "Programmer"
        )

        // isi dataList
        dataList = arrayListOf()
        for (i in imageList.indices) {
            dataList.add(DataClass(imageList[i], namaList[i], keahlianList[i]))
        }

        // set adapter
        recycleView.adapter = AdapterClass(dataList)

        return view
    }
}