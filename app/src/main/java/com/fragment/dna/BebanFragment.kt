package com.fragment.dna

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Adapter.Karyawan
import com.Adapter.KaryawanAdapter
import com.Adapter.Task
import com.example.dna.BebanAdapter
import com.example.dna.BebanClass
import com.example.dna.R
import java.util.Calendar

class BebanFragment : Fragment() {

    private lateinit var calender: Calendar
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var tanggal: EditText

    // Beban kerja summary
    private lateinit var recycleViewBeban: RecyclerView
    private lateinit var bebanList: ArrayList<BebanClass>

    // Daftar karyawan
    private lateinit var recyclerViewKaryawan: RecyclerView
    private lateinit var adapterKaryawan: KaryawanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_beban, container, false)


        // 1. DATE PICKER

        tanggal = view.findViewById(R.id.dateInput)
        calender = Calendar.getInstance()

        tanggal.setOnClickListener {
            showdatePicker(tanggal)
        }


        // 2. BEBAN KERJA SUMMARY


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

        recycleViewBeban = view.findViewById(R.id.recycleViews_bebankerja)
        recycleViewBeban.layoutManager = LinearLayoutManager(requireContext())

        bebanList = arrayListOf()
        for (i in titleList.indices) {
            bebanList.add(BebanClass(titleList[i], angkaList[i]))
        }

        recycleViewBeban.adapter = BebanAdapter(bebanList)



        // 3. LIST KARYAWAN

        recyclerViewKaryawan = view.findViewById(R.id.rvkaryawan)
        recyclerViewKaryawan.layoutManager = LinearLayoutManager(requireContext())

        val karyawanList = mutableListOf<Karyawan>()

// ORANG 1
        val tasksBudi = listOf(
            Task("lapor",2),
            Task("bikin web", 2)
        )

        karyawanList.add(
            Karyawan(
                foto = R.drawable.apasih,
                nama = "Rizqi Aja",
                keahlian = "Admin",
                jamKerja = "8 Jam",
                jumlahTask = tasksBudi.size,
                periode = "20 Nov 2025",
                taskList = tasksBudi
            )
        )

// SET ADAPTER
        adapterKaryawan = KaryawanAdapter(karyawanList)
        recyclerViewKaryawan.adapter = adapterKaryawan

// ORANG 2
        val tasksSiti = listOf(
            Task("Input data",1),
            Task("cek stok",2)
        )

        karyawanList.add(
            Karyawan(
                foto = R.drawable.apasih ,
                nama = "Rizqi Putra",
                keahlian = "Finance",
                jamKerja = "7 Jam",
                jumlahTask = tasksSiti.size,
                periode = "21 Nov 2025",
                taskList = tasksSiti
            )
        )

        adapterKaryawan.notifyItemInserted(karyawanList.size - 1)


        adapterKaryawan = KaryawanAdapter(karyawanList )
        recyclerViewKaryawan.adapter = adapterKaryawan

        return view
    }

    private fun showdatePicker(tanggal: EditText) {
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog(requireContext(), { _, y, m, d ->
            tanggal.setText("$d/${m + 1}/$y")
        }, year, month, day)

        datePickerDialog.show()
    }
}