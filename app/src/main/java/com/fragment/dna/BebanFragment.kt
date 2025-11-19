package com.fragment.dna

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dna.AdapterClass
import com.example.dna.BebanAdapter
import com.example.dna.BebanClass
import com.example.dna.DataClass
import com.example.dna.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.Calendar


class BebanFragment : Fragment() {

    private lateinit var calender : Calendar
    private lateinit var datePickerDialog: DatePickerDialog

    private lateinit var tanggal: EditText

//    Beban kerja
    private lateinit var recycleView: RecyclerView
    private lateinit var bebanList: ArrayList<BebanClass>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_beban, container, false)

        tanggal = view.findViewById(R.id.dateInput)

        calender = Calendar.getInstance()

        tanggal.setOnClickListener {
            showdatePicker(tanggal)

        }

        // inisialisasi RecyclerView
        recycleView = view.findViewById(R.id.recycleViews_bebankerja)
        recycleView.layoutManager = LinearLayoutManager(requireContext())
        recycleView.setHasFixedSize(true)

        // data

        val titleList = arrayOf(
            "Total Karyawan",
            "Overload",
            "Normal",
            "Underload",
            "Rata-Rata Jam"
        )

        val angkaList = arrayOf(
            "8",
            "1",
            "2",
            "3",
            "128 Jam"
        )



        // isi dataList
        bebanList = arrayListOf()
        for (i in titleList.indices) {
            bebanList.add(BebanClass( titleList[i], angkaList[i]))
        }

        // set adapter
        recycleView.adapter = BebanAdapter(bebanList)

        return view
    }

    private fun showdatePicker(tanggal : EditText) {
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)

        datePickerDialog  = DatePickerDialog (requireContext(), {_, year,month,day ->
            val selectedDate = "$day/${month+1}/$year"
            tanggal.setText(selectedDate)
        }, year,month,day)
        datePickerDialog.show()
    }


}
