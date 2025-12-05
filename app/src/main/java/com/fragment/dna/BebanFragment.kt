package com.fragment.dna

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Adapter.*
import com.example.dna.BebanAdapter
import com.example.dna.BebanClass
import com.example.dna.R
import com.example.dna.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import com.DetailTaskAdapter.detailTaskModel
import com.RangeAPI.Assignee
import com.RangeAPI.RangeApiResponse
import java.text.SimpleDateFormat
import java.util.Locale
import com.RangeAPI.RetrofitClient as RangeRetrofitClient
import com.RangeAPI.Task as ApiTask

class BebanFragment : Fragment() {

    private lateinit var recycleViewBeban: RecyclerView
    private lateinit var bebanAdapter: BebanAdapter
    private lateinit var recyclerViewKaryawan: RecyclerView
    private lateinit var adapterKaryawan: KaryawanAdapter
    private val originalKaryawanList = mutableListOf<Karyawan>()
    private lateinit var autoCompleteKaryawan: AutoCompleteTextView
    private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private var currentStartDate: String = ""
    private var currentEndDate: String = ""
    private lateinit var tanggal: EditText
    private lateinit var tanggal_akhir: EditText
    private lateinit var calendar: Calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_beban, container, false)

        tanggal = view.findViewById(R.id.dateInput)
        tanggal_akhir = view.findViewById(R.id.dateInput_akhir)
        calendar = Calendar.getInstance()
        tanggal.setOnClickListener { showdatePicker(tanggal) }
        tanggal_akhir.setOnClickListener { showdatePicker_akhir(tanggal_akhir) }

        recycleViewBeban = view.findViewById(R.id.recycleViews_bebankerja)
        recycleViewBeban.layoutManager = GridLayoutManager(requireContext(), 2)
        val bebanList = arrayListOf(
            BebanClass("Normal", "0"),
            BebanClass("Overload", "0"),
            BebanClass("Underload", "0"),
        )
        bebanAdapter = BebanAdapter(bebanList)
        recycleViewBeban.adapter = bebanAdapter

        recyclerViewKaryawan = view.findViewById(R.id.rvkaryawan)
        recyclerViewKaryawan.layoutManager = LinearLayoutManager(requireContext())
        adapterKaryawan = KaryawanAdapter(mutableListOf())
        recyclerViewKaryawan.adapter = adapterKaryawan
        autoCompleteKaryawan = view.findViewById(R.id.cariNama)

        val cal = Calendar.getInstance()

        cal.set(Calendar.DAY_OF_MONTH, 1)
        currentStartDate = apiDateFormat.format(cal.time)
        tanggal.setText(SimpleDateFormat("d/M/yyyy", Locale.US).format(cal.time))

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        currentEndDate = apiDateFormat.format(cal.time)
        tanggal_akhir.setText(SimpleDateFormat("d/M/yyyy", Locale.US).format(cal.time))

        fetchDataFromRangeApi()

        return view
    }

    private fun setupDrowdownAndListener() {
        val daftarKaryawanItem = originalKaryawanList.map { karyawan ->
            KaryawanItems(namas = karyawan.nama)
        }.toMutableList()

        val semuaKaryawanItem = KaryawanItems(namas = "Semua Karyawan", isChecked = true)
        daftarKaryawanItem.add(0, semuaKaryawanItem)

        val dropdownAdapter = KaryawanItemsAdapter(requireContext(), daftarKaryawanItem)
        autoCompleteKaryawan.setAdapter(dropdownAdapter)
        autoCompleteKaryawan.setText("Semua Karyawan", false)

        autoCompleteKaryawan.setOnItemClickListener { parent, _, position, _ ->
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
                adapter.items.firstOrNull { it.namas == "Semua Karyawan" }
                    ?.let { it.isChecked = false }
            }

            updateFilterState(adapter)
            autoCompleteKaryawan.showDropDown()
        }
    }

    private fun updateFilterState(adapter: KaryawanItemsAdapter) {
        adapter.notifyDataSetChanged()

        val checkedNames = adapter.items.filter { it.isChecked }.map { it.namas }
        val summaryText: String

        if (checkedNames.contains("Semua Karyawan") || checkedNames.isEmpty()) {
            summaryText = "Semua Karyawan"
            performSearch(listOf("Semua Karyawan"))
        } else {
            summaryText = checkedNames.take(2).joinToString(", ") +
                    if (checkedNames.size > 2) ", ..." else ""
            performSearch(checkedNames)
        }

        autoCompleteKaryawan.setText(summaryText, false)
        autoCompleteKaryawan.setSelection(summaryText.length)
    }

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

    private fun fetchDataFromRangeApi() {
        if (currentStartDate.isEmpty() || currentEndDate.isEmpty()) {
            Log.d("BebanFragment", "Tanggal belum di-set, skip API call.")
            return
        }

        Log.d("BebanFragment", "Fetching data for dates: $currentStartDate to $currentEndDate")
        RangeRetrofitClient.instance.getAssigneeTasks(
            startDate = currentStartDate,
            endDate = currentEndDate
        ).enqueue(object : Callback<RangeApiResponse> {
            override fun onResponse(
                call: Call<RangeApiResponse>,
                response: Response<RangeApiResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val assigneesFromApi = apiResponse?.assignees ?: emptyList()
                    val karyawanListFromApi = mapAssigneesToKaryawan(assigneesFromApi)
                    originalKaryawanList.clear()
                    originalKaryawanList.addAll(karyawanListFromApi)
                    adapterKaryawan.updateList(originalKaryawanList)
                    setupDrowdownAndListener()
//                    updateBebanSummary(originalKaryawanList)
                } else {
                    Log.e("BebanFragment", "API Error Response: ${response.code()} - ${response.message()}")
                    Log.e("BebanFragment", "Error Body: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<RangeApiResponse>, t: Throwable) {
                Log.e("BebanFragment", "API Failure: ${t.message}", t)
            }
        })
    }

    private fun mapAssigneesToKaryawan(assignees: List<Assignee>): List<Karyawan> {
        return assignees.map { assignee ->
            val detailTasks = assignee.tasks?.map { apiTask ->
                mapApiTaskToDetailTaskModel(apiTask)
            } ?: emptyList()

            val tasksForKaryawan = listOf(
                Task(
                    judul = "Semua Tugas",
                    jumlahTask = detailTasks.size,
                    isExpanded = false,
                    detailTaskList = detailTasks
                )
            )

            Karyawan(
                foto = R.drawable.profile,
                nama = assignee.name ?: "Tanpa Nama",
                keahlian = assignee.email ?: "Tanpa Email",
                jamKerja = "${assignee.tasks?.sumOf { it.timeEstimateHours ?: 0 } ?: 0} Jam",
                jumlahTask = tasksForKaryawan.size,
                periode = "Desember 2025",
                taskList = tasksForKaryawan
            )
        }
    }

    private fun mapApiTaskToDetailTaskModel(apiTask: ApiTask): detailTaskModel {
        return detailTaskModel(
            judul = apiTask.name ?: "Tanpa Judul",
            desc = apiTask.Deskripsi ?: "Tidak ada deskripsi",
            jam = "${apiTask.timeEstimateHours?.toString() ?: "0"} Jam",
            tanggal = apiTask.startDate ?: "-",
            project = "ClickUp",
            progress = apiTask.statusName ?: "Unknown",
            level = "Normal",
            isExpanded = false
        )
    }

//    private fun updateBebanSummary(karyawanList: List<Karyawan>) {
//        val normalCount = karyawanList.count {
//            val totalTasks = it.taskList.sumOf { task -> task.jumlahTask }
//            totalTasks in 5..10
//        }
//        val overloadCount = karyawanList.count {
//            val totalTasks = it.taskList.sumOf { task -> task.jumlahTask }
//            totalTasks > 10
//        }
//        val underloadCount = karyawanList.count {
//            val totalTasks = it.taskList.sumOf { task -> task.jumlahTask }
//            totalTasks < 5
//        }
//
//        val updatedList = arrayListOf(
//            BebanClass("Normal", normalCount.toString()),
//            BebanClass("Overload", overloadCount.toString()),
//            BebanClass("Underload", underloadCount.toString())
//        )
//        bebanAdapter.updateData(updatedList)
//    }

    private fun showdatePicker(tanggalEditText: EditText) {
        val cal = Calendar.getInstance()
        if (tanggalEditText.text.isNotEmpty()) {
            try {
                val date = SimpleDateFormat("d/M/yyyy", Locale.US).parse(tanggalEditText.text.toString())
                if (date != null) cal.time = date
            } catch (e: Exception) {

            }
        }

        DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply { set(year, month, dayOfMonth) }
            tanggalEditText.setText(SimpleDateFormat("d/M/yyyy", Locale.US).format(selectedDate.time))
            currentStartDate = apiDateFormat.format(selectedDate.time)
            fetchDataFromRangeApi()
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun showdatePicker_akhir(tanggalEditText: EditText) {
        val cal = Calendar.getInstance()
        if (tanggalEditText.text.isNotEmpty()) {
            try {
                val date = SimpleDateFormat("d/M/yyyy", Locale.US).parse(tanggalEditText.text.toString())
                if (date != null) cal.time = date
            } catch (e: Exception) {

            }
        }

        DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply { set(year, month, dayOfMonth) }
            tanggalEditText.setText(SimpleDateFormat("d/M/yyyy", Locale.US).format(selectedDate.time))
            currentEndDate = apiDateFormat.format(selectedDate.time)
            fetchDataFromRangeApi()
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }
}
