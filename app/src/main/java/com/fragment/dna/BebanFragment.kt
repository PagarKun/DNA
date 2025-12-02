package com.fragment.dna

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Adapter.*
import com.MemberAPI.MemberRequest
import com.MemberAPI.RetrofitClient
import com.MemberAPI.Todo
import com.example.dna.BebanAdapter
import com.example.dna.BebanClass
import com.example.dna.R
import com.example.dna.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import com.DetailTaskAdapter.detailTaskModel
class BebanFragment : Fragment() {

    // --- DEKLARASI PROPERTI KELAS ---
    private lateinit var recycleViewBeban: RecyclerView
    private lateinit var bebanAdapter: BebanAdapter
    private lateinit var bebanList: ArrayList<BebanClass>

    // Properti untuk daftar Karyawan
    private lateinit var recyclerViewKaryawan: RecyclerView
    private lateinit var adapterKaryawan: KaryawanAdapter
    private val originalKaryawanList = mutableListOf<Karyawan>()

    // Properti Dropdown

    private lateinit var autoCompleteKaryawan: AutoCompleteTextView

    // Properti untuk Date Picker
    private lateinit var tanggal: EditText
    private lateinit var tanggal_akhir: EditText
    private lateinit var calendar: Calendar

    // Variabel API
    private val dataList = ArrayList<MemberRequest>()
    private val fullList = ArrayList<MemberRequest>()
    private val karlist = ArrayList<Karyawan>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_beban, container, false)

        // --- DATE PICKER SETUP ---
        tanggal = view.findViewById(R.id.dateInput)
        tanggal_akhir = view.findViewById(R.id.dateInput_akhir)
        calendar = Calendar.getInstance()
        tanggal.setOnClickListener { showdatePicker(tanggal) }
        tanggal_akhir.setOnClickListener { showdatePicker_akhir(tanggal_akhir) }

        // --- BEBAN SUMMARY SETUP ---
        recycleViewBeban = view.findViewById(R.id.recycleViews_bebankerja)
        recycleViewBeban.layoutManager = GridLayoutManager(requireContext(), 2)
        bebanList = arrayListOf(
            BebanClass("Total Karyawan", "0"),
            BebanClass("Overload", "0"),
        )
        bebanAdapter = BebanAdapter(bebanList)
        recycleViewBeban.adapter = bebanAdapter
        fetchTotalKaryawan()

        // --- KARYAWAN LIST SETUP ---
        recyclerViewKaryawan = view.findViewById(R.id.rvkaryawan)
        recyclerViewKaryawan.layoutManager = LinearLayoutManager(requireContext())


        adapterKaryawan = KaryawanAdapter(mutableListOf(), emptyList())
        recyclerViewKaryawan.adapter = adapterKaryawan

        autoCompleteKaryawan = view.findViewById(R.id.cariNama)

        loadOriginalKaryawanData()


        return view
    }
//    Search Enginge
    private fun setupDrowdownAndListener() {

    val daftarKaryawanItem = originalKaryawanList.map { karyawan ->

        KaryawanItems(namas = karyawan.nama)
    }.toMutableList()

    val semuaKaryawanItem = KaryawanItems(namas = "Semua Karyawan", isChecked = true)
    daftarKaryawanItem.add(0, semuaKaryawanItem)

    val dropdownAdapter = KaryawanItemsAdapter(requireContext(), daftarKaryawanItem)
    autoCompleteKaryawan.setAdapter(dropdownAdapter)
    autoCompleteKaryawan.setText("Semua Karyawan", false)

    autoCompleteKaryawan.setOnItemClickListener { parent, view, position, id ->
        val adapter = parent.adapter as KaryawanItemsAdapter
        val clickedItem = adapter.getItem(position) ?: return@setOnItemClickListener
        clickedItem.isChecked = !clickedItem.isChecked

        if (clickedItem.namas == "Semua Karyawan" && clickedItem.isChecked) {
            adapter.items.forEach { item ->
                if (item.namas != "Semua Karyawan") {
                    item.isChecked = false
                }
            }


        } else if (clickedItem.namas != "Semua Karyawan" && clickedItem.isChecked) {
            adapter.items.firstOrNull { it.namas == "Semua Karyawan" }?.let { semuaKaryawanItem ->
                semuaKaryawanItem.isChecked = false
            }
        }


        if (clickedItem.namas == "Semua Karyawan" && clickedItem.isChecked) {

        }else if (clickedItem.namas != "Semua Karyawan" && clickedItem.isChecked) {

        }
        updateFilterState(adapter)

        autoCompleteKaryawan.showDropDown()
    }

}
    private fun updateFilterState(adapter: KaryawanItemsAdapter) {
        adapter.notifyDataSetChanged()

        val checkedNames = adapter.items.filter {it.isChecked }.map { it.namas }
        val summaryText: String

        if (checkedNames.contains("Semua Karyawan") || checkedNames.isEmpty()){

            summaryText = "Semua Karyawan"
        performSearch(listOf("Semua Karyawan"))

        }else {
            summaryText = checkedNames.take(2).joinToString(", ") +
                    if (checkedNames.size > 2) ", ..." else ""
            performSearch(checkedNames)
        }

        autoCompleteKaryawan.setText(summaryText, false)

        autoCompleteKaryawan.setSelection(summaryText.length)
    }

//    Func Pencarian terpusat

    private fun performSearch(namaKeyword: List<String>) {
        val isShowingAll = namaKeyword.contains("Semua Karyawan") || namaKeyword.isEmpty()

        val hasilPencarian = if (isShowingAll) {
            originalKaryawanList
        } else {
            originalKaryawanList.filter { karyawan ->
                namaKeyword.contains(karyawan.nama)
            }
        }
        adapterKaryawan.updateList(hasilPencarian)
    }

    private fun loadOriginalKaryawanData() {
        originalKaryawanList.clear()

        // --- DATA UNIK UNTUK RIZQI AJA (ORANG 1) ---
        val detailListRizqiAja1 = listOf(
            detailTaskModel(
                "Develop Landing Page",
                "Create responsive landing page",
                "32 Jam",
                "12/12/2025",
                "ClickUp",
                "Selesai",
                "High",
                false),
            detailTaskModel(
                "Code Review",
                "Reviewing",
                "10 Jam", "12/12/2025",
                "ClickUp",
                "Selesai",
                "High",
                false)
        )
        val detailListRizqiAja2 = listOf(
            detailTaskModel(
                "Fix Critical Bug",
                "Database connection issue",
                "8 Jam",
                "15/12/2025",
                "ClickUp",
                "Pending",
                "Critical",
                false),
            detailTaskModel(
                "Write Unit Test",
                "Testing new module",
                "4 Jam",
                "15/12/2025",
                "ClickUp",
                "Selesai",
                "Medium",
                false)
        )


        val tasksRizqiAja = listOf(
            Task(
                "Frontend dan backend",
                detailListRizqiAja1.size,
                false,
                detailListRizqiAja1),
            Task(
                "Bug",
                detailListRizqiAja2.size,
                false,
                detailListRizqiAja2)
        )

        // --- DATA UNIK UNTUK RIZQI PUTRA (ORANG 2) ---
        val detailListRizqiPutra1 = listOf(
            detailTaskModel(
                "Input User Data",
                "Menginput data 1000 user",
                "5 Jam",
                "22/11/2025",
                "Finance App",
                "Selesai",
                "Low",
                false)
        )
        val detailListRizqiPutra2 = listOf(
            detailTaskModel(
                "Cek Stok Harian",
                "Memastikan stok di gudang A",
                "2 Jam",
                "23/11/2025",
                "Warehouse",
                "Pending",
                "Medium",
                false)
        )
        val tasksRizqiPutra = listOf(
            Task(
                "Input data",
                detailListRizqiPutra1.size,
                false,
                detailListRizqiPutra1),
            Task(
                "cek stok",
                detailListRizqiPutra2.size,
                false,
                detailListRizqiPutra2
            )

//            Orang ke 3
        )
        val detailListRizqiAntono = listOf(
            detailTaskModel (
                "Create Splash Screen",
                "Membuat splash screen di android",
                "1 jam ",
                "02/12/2025",
                "PKL",
                "On Going",
                "Easy",
                false
            ),
            detailTaskModel (
                "Dashboard Karyawan",
                "Membuat Dashboard Karyawan",
                "3 jam ",
                "02/12/2025",
                "PKL",
                "On Going",
                "Easy",
                false
            )
        )
        val tasksRizqiAntono= listOf(
            Task(
                "Android Studio",
                detailListRizqiAntono.size,
                false,
                detailListRizqiAntono),
        )

        // Tambahkan data ke daftar asli
        originalKaryawanList.add(
            Karyawan(
                foto = R.drawable.profile,
                nama = "Rizqi Putra",
                keahlian = "Finance",
                jamKerja = "7 Jam",
                jumlahTask = tasksRizqiPutra.size,
                periode = "21 Nov 2025",
                taskList = tasksRizqiPutra
            )
        )
        originalKaryawanList.add(
            Karyawan(
                foto = R.drawable.profile,
                nama = "Rizqi Aja",
                keahlian = "Onwer",
                jamKerja = "8 Jam",
                jumlahTask = tasksRizqiAja.size,
                periode = "20 Nov 2025",
                taskList = tasksRizqiAja
            )
        )

        originalKaryawanList.add(
            Karyawan(
                foto = R.drawable.profile,
                nama = "Rizqi Antono",
                keahlian = "Member",
                jamKerja = "8 Jam",
                jumlahTask = tasksRizqiAntono.size,
                periode = "20 Nov 2025",
                taskList = tasksRizqiAntono
            )
        )

        adapterKaryawan.updateList(originalKaryawanList)

        setupDrowdownAndListener()
    }

    // --- FETCH API TOTAL KARYAWAN
    private fun fetchTotalKaryawan() {
        RetrofitClient.instance.getTodo().enqueue(object : Callback<Todo> {
            override fun onResponse(call: Call<Todo>, response: Response<Todo>) {
                if (response.isSuccessful) {
                    val data = response.body()?.users ?: emptyList()
                    val totalKaryawan = data.size.toString()
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

    // --- DATE PICKER
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
