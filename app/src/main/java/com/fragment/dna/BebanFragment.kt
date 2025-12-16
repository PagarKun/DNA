package com.fragment.dna

import ViewModel.BebanViewModel
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Adapter.*
import com.example.dna.BebanAdapter
import com.example.dna.BebanClass
import com.example.dna.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BebanFragment : Fragment() {

    private lateinit var recycleViewBeban: RecyclerView
    private lateinit var bebanAdapter: BebanAdapter
    private lateinit var recyclerViewKaryawan: RecyclerView
    private lateinit var adapterKaryawan: KaryawanAdapter
    private lateinit var autoCompleteKaryawan: AutoCompleteTextView
    private lateinit var tanggal: EditText
    private lateinit var tanggal_akhir: EditText
    private lateinit var progressBar: ProgressBar

    private val originalKaryawanList = mutableListOf<Karyawan>()

    private val uiDateFormat = SimpleDateFormat("d/M/yyyy", Locale.ENGLISH)
    private val apiDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

    //ViewModel
    private val viewModel: BebanViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_beban, container, false)

        initializeViews(view)

        setupAdapters()
        setupDatePickers()

        setDefaultDateRange()

        observeViewModel()

        viewModel.fetchDataFromApi(
            tanggal.tag.toString(),
            tanggal_akhir.tag.toString()
        )

        return view
    }

    private fun initializeViews(view: View) {
        tanggal = view.findViewById(R.id.dateInput)
        tanggal_akhir = view.findViewById(R.id.dateInput_akhir)
        recycleViewBeban = view.findViewById(R.id.recycleViews_bebankerja)
        recyclerViewKaryawan = view.findViewById(R.id.rvkaryawan)
        autoCompleteKaryawan = view.findViewById(R.id.cariNama)
        progressBar = view.findViewById(R.id.progressBar)
    }

    private fun setupAdapters() {

        recycleViewBeban.layoutManager = GridLayoutManager(requireContext(), 2)
        val bebanList = arrayListOf(
            BebanClass("Normal", "0"),
            BebanClass("Overload", "0"),
            BebanClass("Underload", "0"),
        )
        bebanAdapter = BebanAdapter(bebanList)
        recycleViewBeban.adapter = bebanAdapter


        recyclerViewKaryawan.layoutManager = LinearLayoutManager(requireContext())
        adapterKaryawan = KaryawanAdapter(mutableListOf()) { karyawan ->

            val detailFragment = DetailTaskFragment().apply {
                arguments = Bundle().apply {
                    putInt("EMPLOYEE_ID", karyawan.id)
                    putString("START_DATE", tanggal.tag.toString())
                    putString("END_DATE", tanggal_akhir.tag.toString())
                }
            }
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, detailFragment)
                addToBackStack(null)
                commit()
            }
        }
        recyclerViewKaryawan.adapter = adapterKaryawan
    }

    private fun setupDatePickers() {
        tanggal.setOnClickListener { showDatePickerDialog(tanggal, true) }
        tanggal_akhir.setOnClickListener { showDatePickerDialog(tanggal_akhir, false) }
    }

    private fun setDefaultDateRange() {
        val cal = Calendar.getInstance()

        // tanggal awal
        cal.set(Calendar.DAY_OF_MONTH, 1)
        tanggal.setText(uiDateFormat.format(cal.time))
        tanggal.tag = apiDateFormat.format(cal.time) // Simpan format API di tag

        // tanggal akhir
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        tanggal_akhir.setText(uiDateFormat.format(cal.time))
        tanggal_akhir.tag = apiDateFormat.format(cal.time) // Simpan format API di tag
    }


    private fun observeViewModel() {

        viewModel.karyawanList.observe(viewLifecycleOwner) { karyawanList ->
            karyawanList?.let {
                Log.d("BebanFragment", "Data diterima dari ViewModel: ${it.size} item.")
                originalKaryawanList.clear()
                originalKaryawanList.addAll(it)


                performSearch(listOf("Semua Karyawan"))


                setupDropdownAndListener()
            }
        }


        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            recyclerViewKaryawan.visibility = if (isLoading) View.GONE else View.VISIBLE
        }


        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                Log.e("BebanFragment", "Error dari ViewModel: $it")

            }
        }
    }

    private fun showDatePickerDialog(dateEditText: EditText, isStartDate: Boolean) {
        val cal = Calendar.getInstance()
        try {
            uiDateFormat.parse(dateEditText.text.toString())?.let { cal.time = it }
        } catch (e: Exception) {

        }

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply { set(year, month, dayOfMonth) }
                dateEditText.setText(uiDateFormat.format(selectedDate.time))
                dateEditText.tag = apiDateFormat.format(selectedDate.time)


                viewModel.clearData()
                viewModel.fetchDataFromApi(
                    tanggal.tag.toString(),
                    tanggal_akhir.tag.toString()
                )
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }


    private fun setupDropdownAndListener() {
        if (originalKaryawanList.isEmpty()) {
            autoCompleteKaryawan.setAdapter(null)
            return
        }

        val daftarKaryawanItem = originalKaryawanList.map { KaryawanItems(namas = it.nama) }.toMutableList()
        daftarKaryawanItem.add(0, KaryawanItems(namas = "Semua Karyawan", isChecked = true))

        val dropdownAdapter = KaryawanItemsAdapter(requireContext(), daftarKaryawanItem)
        autoCompleteKaryawan.setAdapter(dropdownAdapter)
        autoCompleteKaryawan.setText("Semua Karyawan", false)

        autoCompleteKaryawan.setOnItemClickListener { parent, _, position, _ ->
            val adapter = parent.adapter as KaryawanItemsAdapter
            val clickedItem = adapter.getItem(position) ?: return@setOnItemClickListener
            clickedItem.isChecked = !clickedItem.isChecked

            if (clickedItem.namas == "Semua Karyawan" && clickedItem.isChecked) {
                adapter.items.forEach { if (it.namas != "Semua Karyawan") it.isChecked = false }
            } else if (clickedItem.namas != "Semua Karyawan" && clickedItem.isChecked) {
                adapter.items.firstOrNull { it.namas == "Semua Karyawan" }?.isChecked = false
            }

            updateFilterState(adapter)
            autoCompleteKaryawan.showDropDown()
        }
    }

    private fun updateFilterState(adapter: KaryawanItemsAdapter) {
        adapter.notifyDataSetChanged()

        val checkedNames = adapter.items.filter { it.isChecked }.map { it.namas }

        val summaryText = if (checkedNames.contains("Semua Karyawan") || checkedNames.isEmpty()) {
            performSearch(listOf("Semua Karyawan"))
            "Semua Karyawan"
        } else {
            performSearch(checkedNames)
            checkedNames.take(2).joinToString(", ") + if (checkedNames.size > 2) ", ..." else ""
        }

        autoCompleteKaryawan.setText(summaryText, false)
        autoCompleteKaryawan.setSelection(summaryText.length)
    }

    private fun performSearch(namaKeyword: List<String>) {
        val isShowingAll = namaKeyword.contains("Semua Karyawan") || namaKeyword.isEmpty()
        val hasilPencarian = if (isShowingAll) {
            originalKaryawanList
        } else {
            originalKaryawanList.filter { namaKeyword.contains(it.nama) }
        }
        adapterKaryawan.updateList(hasilPencarian)
    }
}
