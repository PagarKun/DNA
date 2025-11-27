package com.fragment.dna

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Adapter.Karyawan
import com.Adapter.KaryawanAdapter
import com.Adapter.Task
import com.Adapter.detailBeban
import com.Adapter.detailBebanAdapter
import com.MemberAPI.MemberRequest
import com.MemberAPI.RetrofitClient
import com.MemberAPI.Todo
import com.Nyoba.Employee
import com.Nyoba.EmployeeAdapter
import com.example.dna.BebanAdapter
import com.example.dna.BebanClass
import com.example.dna.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import com.example.dna.AdapterClass

class BebanFragment : Fragment() {

    private lateinit var recycleViewBeban: RecyclerView
    private lateinit var bebanAdapter: BebanAdapter
    private lateinit var bebanList: ArrayList<BebanClass>
    private val dataList = ArrayList<MemberRequest>()
    private val fullList = ArrayList<MemberRequest>()


    private lateinit var recyclerViewKaryawan: RecyclerView
    private lateinit var adapterKaryawan: KaryawanAdapter

    private lateinit var tanggal: EditText
    private lateinit var tanggal_akhir: EditText
    private lateinit var calendar: Calendar


//    detail beban karyawan
    private lateinit var rvBeban: RecyclerView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_beban, container, false)



        // DATE PICKER

        //  Tanggal Mulai
        tanggal = view.findViewById(R.id.dateInput)
        calendar = Calendar.getInstance()

        tanggal.setOnClickListener {
            showdatePicker(tanggal)
        }

        //  Tanggal Akhir
        tanggal_akhir = view.findViewById(R.id.dateInput_akhir)
        calendar = Calendar.getInstance()

        tanggal_akhir.setOnClickListener {
            showdatePicker_akhir(tanggal_akhir)
        }

        // BEBAN SUMMARY SETUP

        recycleViewBeban = view.findViewById(R.id.recycleViews_bebankerja)
        recycleViewBeban.layoutManager = GridLayoutManager(requireContext(), 2)

        // Dummy Card Total karyawan
        bebanList = arrayListOf(
            BebanClass("Total Karyawan", "0"),
            BebanClass("Overload", "0"),
            BebanClass("Normal", "0"),
            BebanClass("Underload", "0"),
            BebanClass("Rata-Rata Jam", "0"),
            BebanClass("Yang akan datang", "0")
        )

        bebanAdapter = BebanAdapter(bebanList)
        recycleViewBeban.adapter = bebanAdapter

        // Panggil API
        fetchTotalKaryawan()



        // KARYAWAN LIST


        recyclerViewKaryawan = view.findViewById(R.id.rvkaryawan)
        recyclerViewKaryawan.layoutManager = LinearLayoutManager(requireContext())

        val karyawanList = mutableListOf<Karyawan>()
        val detailbebanList = mutableListOf<detailBeban>()

        // ORANG 1
        val tasksBudi = listOf(
            Task("Frontend dan backend", 2,false),
            Task("Bug", 2,false)
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



        adapterKaryawan = KaryawanAdapter(karyawanList,detailbebanList)
        recyclerViewKaryawan.adapter = adapterKaryawan

        // ORANG 2
        val tasksSiti = listOf(
            Task("Input data", 1,false),
            Task("cek stok", 2,false)
        )

        karyawanList.add(
            Karyawan(
                foto = R.drawable.apasih,
                nama = "Rizqi Putra",
                keahlian = "Finance",
                jamKerja = "7 Jam",
                jumlahTask = tasksSiti.size,
                periode = "21 Nov 2025",
                taskList = tasksSiti
            )
        )

        adapterKaryawan.notifyItemInserted(karyawanList.size - 1)



        return view
    }



    // FETCH API TOTAL KARYAWAN

    private fun fetchTotalKaryawan() {

        RetrofitClient.instance.getTodo().enqueue(object : Callback<Todo> {

            override fun onResponse(call: Call<Todo>, response: Response<Todo>) {

                if (response.isSuccessful) {
                    Log.d("API_KARYAWAN","Respon Raw: ${response.body()}")

                    val data = response.body()?.users ?:
                    emptyList()
                    var totalKaryawan = data.size.toString()

                    val updatedList = arrayListOf(
                        BebanClass("Total Karyawan", totalKaryawan),
                        BebanClass("Overload", "2"),
                        BebanClass("Normal", "2"),
                        BebanClass("Underload", "2"),
                        BebanClass("Rata-Rata Jam", "128 Jam"),
                        BebanClass("Yang akan datang", "128 Jam")
                    )
                    fullList.clear()
                    fullList.addAll(data)
                    dataList.clear()
                    dataList.addAll(data)


                    bebanAdapter.updateData(updatedList)
                }
            }

            override fun onFailure(call: Call<Todo>, t: Throwable) {
                Log.e("API", "Error: ${t.message}")
            }
        })
    }

    private fun showdatePicker(tanggal: EditText) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, y, m, d ->
            tanggal.setText("$d/${m + 1}/$y")
        }, year, month, day).show()
    }
    private fun showdatePicker_akhir(tanggal_akhir: EditText) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, y, m, d ->
            tanggal_akhir.setText("$d/${m + 1}/$y")
        }, year, month, day).show()
    }

}