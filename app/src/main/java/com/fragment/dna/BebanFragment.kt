package com.fragment.dna

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.core.graphics.green
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
    private val apiDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
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

//        DetailTaskActivity
        recyclerViewKaryawan.layoutManager = LinearLayoutManager(requireContext())
        adapterKaryawan = KaryawanAdapter(mutableListOf()) { karyawan ->

            val detailFragment = DetailTaskFragment()
            val bundle = Bundle()
            bundle.putInt("EMPLOYEE_ID", karyawan.id)
            bundle.putString("START_DATE", currentStartDate)
            bundle.putString("END_DATE", currentEndDate)
            detailFragment.arguments = bundle

            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, detailFragment) // Pastikan ID container-mu benar
                addToBackStack(null)
                commit()
            }
        }

        recyclerViewKaryawan.adapter = adapterKaryawan


        autoCompleteKaryawan = view.findViewById(R.id.cariNama)

        val cal = Calendar.getInstance()

        cal.set(Calendar.DAY_OF_MONTH, 1)
        currentStartDate = apiDateFormat.format(cal.time)
        tanggal.setText(SimpleDateFormat("d/M/yyyy", Locale.ENGLISH).format(cal.time))

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        currentEndDate = apiDateFormat.format(cal.time)
        tanggal_akhir.setText(SimpleDateFormat("d/M/yyyy", Locale.ENGLISH).format(cal.time))

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

                } else {
                    Log.e("BebanFragment", "API Error Response: ${response.code()} - ${response.message()}")
                    Log.e("BebanFragment", "Error Body: ${response.errorBody()?.string()}")

                    val dummyList = getDummyKaryawanList()
                    originalKaryawanList.clear()
                    originalKaryawanList.addAll(dummyList)
                    adapterKaryawan.updateList(originalKaryawanList)
                    setupDrowdownAndListener()
                }
            }

            override fun onFailure(call: Call<RangeApiResponse>, t: Throwable) {
                Log.e("BebanFragment", "API Failure: ${t.message}", t)


                val dummyList = getDummyKaryawanList()
                originalKaryawanList.clear()
                originalKaryawanList.addAll(dummyList)
                adapterKaryawan.updateList(originalKaryawanList)
                setupDrowdownAndListener()
            }
        })
    }

    private fun mapAssigneesToKaryawan(assignees: List<Assignee>): List<Karyawan> {
        return assignees.map { assignee ->


            val percentageValue = assignee.onTimePercentage ?: 0.0F

            val backgroundColorResource : Int
            val textColorAndImageResource: Int

             if (percentageValue < 80.0F) {
                backgroundColorResource = R.color.red3
                textColorAndImageResource = R.color.red
            } else {
                 backgroundColorResource = R.color.hijau3
                 textColorAndImageResource = R.color.hijaugelap
            }

            val onTimePercentageString = String.format("%.0f%%", percentageValue)


            Karyawan(
                id = assignee.clickupId ?: 0 ,
                foto = R.drawable.profile,
                nama = assignee.name ?: "Tanpa Nama",
                keahlian = assignee.role ?: "Role tidak Ditemukan",
                jamKerja = "${assignee.tasks?.sumOf { it.timeEstimateHours ?: 0 } ?: 0} Jam",
                periode = "Desember 2025",
                taskList = emptyList(),
                totalTaskFromApi = assignee.totalTask ?: 0,
                totalSpentHoursFromApi = assignee.totalSpentHours ?: 0,
                totalActualHoursFormApi = assignee.actualWorkHours ?: 0,
                performanceColor = backgroundColorResource,
                peformanceTextColor = textColorAndImageResource,
                onTimepersentase = onTimePercentageString
            )
        }
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

    private fun getDummyKaryawanList(): List<Karyawan> {
        Log.d("BebanFragment", "API call failed. Generating dummy data.")


        val dummyApiTask1 = ApiTask(
            id = "dummy-01", name = "Contoh: Menganalisis data penjualan",
            deskripsi = "Menganalisis data penjualan kuartal terakhir untuk menemukan tren.",
            priority = "okeje",
            timeSpentHours = 8, startDate = "2025-12-01",
            statusName = "In Progress",
            projectName = "Skibidi",
            dueDate = null,
            dateDone = null,
            timeEstimateHours = 10,
            timeEstimate = null,
            timeEfficiencyPercentage = 0.0F,
            backgroundColor = R.color.red,
            remainingTime = "lebih dari 10 Jam"
        )
        val dummyApiTask2 = ApiTask(
            id = "dummy-02", name = "Contoh: Membuat laporan bulanan",
            deskripsi = "Menyusun laporan performa bulanan untuk manajemen.",
            priority = "okeje",
            timeSpentHours = 4,
            startDate = "2025-12-03",
            statusName = "To Do",
            projectName = "Skibidi",
            dueDate = null,
            dateDone = null,
            timeEstimateHours = 5,
            timeEstimate = null,
            timeEfficiencyPercentage = 0.0F,
            backgroundColor = R.color.red,
            remainingTime = "lebih dari 10 Jam"
        )

        val dummyTaskList = listOf(
            Task(
                judul = "Semua Tugas",
                jumlahTask = 2,
                isExpanded = false,
                detailTaskList = listOf(dummyApiTask1, dummyApiTask2)
            )
        )

        val karyawanDummy1 = Karyawan(
            id = 1,
            foto = R.drawable.profile,
            nama = "(Dummy)",
            keahlian = "Pengembang Aplikasi",
            jamKerja = "12 Jam",
            periode = "Desember 2025",
            taskList = dummyTaskList,
            totalTaskFromApi = 2,
            totalSpentHoursFromApi = 16,
            totalActualHoursFormApi = 0,
            performanceColor = R.color.red,
            peformanceTextColor = R.color.hijaugelap,
            onTimepersentase = "0%"


        )

        return listOf(karyawanDummy1)
    }


    private fun showdatePicker(tanggalEditText: EditText) {
        val cal = Calendar.getInstance()
        if (tanggalEditText.text.isNotEmpty()) {
            try {
                val date = SimpleDateFormat("d/M/yyyy", Locale.ENGLISH).parse(tanggalEditText.text.toString())
                if (date != null) cal.time = date
            } catch (e: Exception) {

            }
        }

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                tanggalEditText.setText(SimpleDateFormat("d/M/yyyy", Locale.ENGLISH).format(selectedDate.time))
                currentStartDate = apiDateFormat.format(selectedDate.time)
                fetchDataFromRangeApi()
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showdatePicker_akhir(tanggalEditText: EditText) {
        val cal = Calendar.getInstance()
        if (tanggalEditText.text.isNotEmpty()) {
            try {
                val date = SimpleDateFormat("d/M/yyyy", Locale.ENGLISH).parse(tanggalEditText.text.toString())
                if (date != null) cal.time = date
            } catch (e: Exception) {
            }
        }

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {

                    set(year, month, dayOfMonth)
                }
                tanggalEditText.setText(SimpleDateFormat("d/M/yyyy", Locale.ENGLISH).format(selectedDate.time))
                currentEndDate = apiDateFormat.format(selectedDate.time)
                fetchDataFromRangeApi()
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}
