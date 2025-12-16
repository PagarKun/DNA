package com.fragment.dna

import StatusKaryawan.StatusKaryawanAdapter
import StatusKaryawan.StatusKaryawanModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.MemberAPI.MemberRequest
import com.MemberAPI.PosisiItem
import com.MemberAPI.RetrofitClient
import com.MemberAPI.Todo
import com.example.dna.AdapterClass
import com.example.dna.PosisiAdapter
import com.example.dna.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashBoardFragment : Fragment() {

    private lateinit var recycleView: RecyclerView
    private lateinit var adapter: AdapterClass
    private val fullList = ArrayList<MemberRequest>()

    // Deklarasi semua View
    private lateinit var dropdownPosisi: AutoCompleteTextView
    private lateinit var dropdownStatus: AutoCompleteTextView
    private lateinit var searchInput: EditText
    private lateinit var totalkaryawanText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("DEBUG_FLOW ", "Fragment created")
        val view = inflater.inflate(R.layout.fragment_dash_board, container, false)

        recycleView = view.findViewById(R.id.recycleViews)
        dropdownPosisi = view.findViewById(R.id.autoComplete_posisi)
        dropdownStatus = view.findViewById(R.id.autoComplete_Status)
        searchInput = view.findViewById(R.id.searchInput)
        totalkaryawanText = view.findViewById(R.id.jmlhkaryawan)

        //  Setup RecyclerView
        setupRecyclerView()

        //  Setup Listener untuk semua filter
        setupFilterListeners()

        //  Panggil API
        fetchDataFromApi()

        return view
    }

    private fun setupRecyclerView() {
        recycleView.layoutManager = LinearLayoutManager(requireContext())
        // Inisialisasi adapter list kosong
        adapter = AdapterClass(ArrayList()) { item ->
            showBottomSheetDetail(item)
        }
        recycleView.adapter = adapter
    }

    private fun setupFilterListeners() {
        // Listener kotak Pencarian Nama
        searchInput.addTextChangedListener { text ->
            val keyword = text.toString()

            val posisiAdapter = dropdownPosisi.adapter as? PosisiAdapter
            val statusAdapter = dropdownStatus.adapter as? StatusKaryawanAdapter

            val checkedPositions = posisiAdapter?.items?.filter { it.isChecked }?.map { it.nama }
            val checkedStatuses = statusAdapter?.items?.filter { it.isChecked }?.map { it.status }

            filterData(
                keyword,
                checkedPositions ?: listOf("Semua Posisi"),
                checkedStatuses ?: listOf("Semua Status")
            )
        }
    }

    private fun fetchDataFromApi() {
        Log.d("DEBUG_FLOW", "Calling API......")
        RetrofitClient.instance.getTodo().enqueue(object : Callback<Todo> {
            override fun onResponse(call: Call<Todo>, response: Response<Todo>) {
                Log.d("DEBUG_FLOW", "API onResponse triggered")

                if (response.isSuccessful) {
                    val data = response.body()?.users ?: emptyList()
                    Log.d("API_DATA", "Data received: ${data.size} items")

                    // RecyclerView
                    fullList.clear()
                    fullList.addAll(data)

                    // semua data awal
                    adapter.filterList(fullList)

                    // total karyawan awal
                    totalkaryawanText.text = "${fullList.size} Orang"

                    // Setup dropdown dengan data dari API
                    setupDropdowns(data)

                } else {
                    Log.d("API_DATA", "Error body: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Todo>, t: Throwable) {
                Log.d("DEBUG_FLOW", "API FAILED: ${t.message}")
            }
        })
    }

    private fun setupDropdowns(data: List<MemberRequest>) {
        // Ekstrak data posisi dan status dari API
        val daftarNamaPosisi = data.mapNotNull { it.role }.distinct()
        val daftarStatus = data.mapNotNull { it.status }.distinct()

        // daftar item Posisi
        val daftarPosisiItem = daftarNamaPosisi.map { PosisiItem(it) }.toMutableList()
        daftarPosisiItem.add(0, PosisiItem("Semua Posisi", true))

        // daftar item Status
        val daftarStatusItem =
            daftarStatus.map { StatusKaryawanModel(it, isChecked = false) }.toMutableList()
        daftarStatusItem.add(0, StatusKaryawanModel("Semua Status", true))

        // Inisialisasi Adapter untuk kedua dropdown
        val dropdownAdapterPosisi =
            PosisiAdapter(requireContext(), R.layout.dropdown_posisi_karyawan, daftarPosisiItem)
        val dropdownAdapterStatus = StatusKaryawanAdapter(
            requireContext(),
            R.layout.dropdown_posisi_karyawan,
            daftarStatusItem
        )

        dropdownPosisi.setAdapter(dropdownAdapterPosisi)
        dropdownStatus.setAdapter(dropdownAdapterStatus)

        // Set kedua dropdown
        dropdownPosisi.setText("Semua Posisi", false)
        dropdownStatus.setText("Semua Status", false)


        // Listener dropdown STATUS
        dropdownStatus.setOnItemClickListener { parent, _, position, _ ->
            val adapter = parent.adapter as StatusKaryawanAdapter
            val clickedItem = adapter.getItem(position) ?: return@setOnItemClickListener

            clickedItem.isChecked = !clickedItem.isChecked

            // Logika "Semua Status"
            if (clickedItem.status == "Semua Status" && clickedItem.isChecked) {
                adapter.items.forEach { if (it.status != "Semua Status") it.isChecked = false }
            } else if (clickedItem.status != "Semua Status" && clickedItem.isChecked) {
                adapter.items.firstOrNull { it.status == "Semua Status" }?.isChecked = false
            }
            adapter.notifyDataSetChanged()

            // Buat summary
            val checkedItems = adapter.items.filter { it.isChecked }.map { it.status }
            val summaryText = when {
                checkedItems.contains("Semua Status") || checkedItems.isEmpty() -> "Semua Status"
                else -> checkedItems.take(2)
                    .joinToString(", ") + if (checkedItems.size > 2) ", ..." else ""
            }
            dropdownStatus.setText(summaryText, false)

            val namaKeyword = searchInput.text.toString()
            val posisiAdapter = dropdownPosisi.adapter as PosisiAdapter
            val checkedPositions = posisiAdapter.items.filter { it.isChecked }.map { it.nama }

            filterData(
                namaKeyword,
                checkedPositions.ifEmpty { listOf("Semua Posisi") },
                checkedItems.ifEmpty { listOf("Semua Status") }
            )

            dropdownStatus.showDropDown()
        }

        // Listener dropdown Posisi
        dropdownPosisi.setOnItemClickListener { parent, _, position, _ ->
            val adapter = parent.adapter as PosisiAdapter
            val clickedItem = adapter.getItem(position) ?: return@setOnItemClickListener

            clickedItem.isChecked = !clickedItem.isChecked

            // Logika "Semua Posisi"
            if (clickedItem.nama == "Semua Posisi" && clickedItem.isChecked) {
                adapter.items.forEach { if (it.nama != "Semua Posisi") it.isChecked = false }
            } else if (clickedItem.nama != "Semua Posisi" && clickedItem.isChecked) {
                adapter.items.firstOrNull { it.nama == "Semua Posisi" }?.isChecked = false
            }
            adapter.notifyDataSetChanged()

            // tampilkan summary
            val checkedItems = adapter.items.filter { it.isChecked }.map { it.nama }
            val summaryText = when {
                checkedItems.contains("Semua Posisi") || checkedItems.isEmpty() -> "Semua Posisi"
                else -> checkedItems.take(2)
                    .joinToString(", ") + if (checkedItems.size > 2) ", ..." else ""
            }
            dropdownPosisi.setText(summaryText, false)


            val namaKeyword = searchInput.text.toString()
            val statusAdapter = dropdownStatus.adapter as StatusKaryawanAdapter
            val checkedStatuses = statusAdapter.items.filter { it.isChecked }.map { it.status }

            filterData(
                namaKeyword,
                checkedItems.ifEmpty { listOf("Semua Posisi") },
                checkedStatuses.ifEmpty { listOf("Semua Status") }
            )

            dropdownPosisi.showDropDown()
        }
    }

    private fun filterData(
        namaKeyword: String,
        posisiKeywords: List<String>,
        statusKeywords: List<String>
    ) {
        //daftar lengkap setiap kali filter dipanggil
        var hasilFilter = fullList.toList()

        //  Filter Nama
        if (namaKeyword.isNotBlank()) {
            hasilFilter = hasilFilter.filter { member ->
                // Cari di nama asli ATAU display name.
                val nameMatch = member.name?.contains(namaKeyword, ignoreCase = true)?: false
                val usernameMatch = member.email?.contains(namaKeyword, ignoreCase = true)?: false
                nameMatch || usernameMatch
            }
        }

        //  POSISI (Multi-choice)
        val isFilteringByPosition =
            !posisiKeywords.contains("Semua Posisi") && posisiKeywords.isNotEmpty()
        if (isFilteringByPosition) {
            hasilFilter = hasilFilter.filter { member ->
                posisiKeywords.any { keyword ->
                    member.role?.equals(
                        keyword,
                        ignoreCase = true
                    ) == true
                }
            }
        }

        // (Multi-choice)
        val isFilteringByStatus =
            !statusKeywords.contains("Semua Status") && statusKeywords.isNotEmpty()
        if (isFilteringByStatus) {
            hasilFilter = hasilFilter.filter { member ->
                statusKeywords.any { keyword ->
                    member.status?.equals(
                        keyword,
                        ignoreCase = true
                    ) == true
                }
            }
        }

        // jumlah karyawan
        adapter.filterList(hasilFilter)
        totalkaryawanText.text = "${hasilFilter.size} Orang"
    }

    fun showBottomSheetDetail(item: MemberRequest) {
        val bottomSheet = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.popup_detail_karyawan, null)

        val nama = view.findViewById<TextView>(R.id.namaDetail)
        val email = view.findViewById<TextView>(R.id.email_detail)
        val keahlian = view.findViewById<TextView>(R.id.keahlianDetail)

        nama.text = item.name
        email.text = item.email
        keahlian.text = item.role

        bottomSheet.setContentView(view)
        bottomSheet.behavior.peekHeight = 900
        bottomSheet.show()
    }
}
