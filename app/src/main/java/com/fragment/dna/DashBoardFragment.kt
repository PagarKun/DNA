package com.fragment.dna

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
    private val dataList = ArrayList<MemberRequest>()
    private lateinit var adapter: AdapterClass
    private val fullList = ArrayList<MemberRequest>()

    // Deklarasi untuk semua View
    private lateinit var dropdownPosisi: AutoCompleteTextView
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
        searchInput = view.findViewById(R.id.searchInput)
        totalkaryawanText = view.findViewById(R.id.jmlhkaryawan)

        // 2. Setup RecyclerView
        setupRecyclerView()

        // 3. Setup Listener untuk filter
        setupFilterListeners()

        // 4. Panggil API
        fetchDataFromApi()

        return view
    }

    private fun setupRecyclerView() {
        recycleView.layoutManager = LinearLayoutManager(requireContext())
        adapter = AdapterClass(dataList) { item ->
            showBottomSheetDetail(item)
        }
        recycleView.adapter = adapter
    }

    private fun setupFilterListeners() {
        // Listener kotak pencarian nama
        searchInput.addTextChangedListener { text ->
            // Ambil keyword dari teks yang diketik
            val keyword = text.toString()

            // untuk mengakses status checkbox saat ini
            val posisiAdapter = dropdownPosisi.adapter as? PosisiAdapter ?: return@addTextChangedListener

            //semua posisi yang sedang tercentang
            val checkedPositions = posisiAdapter.items.filter { it.isChecked }.map { it.nama }

            // filterData (multi-choice)
            if (checkedPositions.contains("Semua Posisi") || checkedPositions.isEmpty()) {
                filterData(keyword, listOf("Semua Posisi"))
            } else {
                filterData(keyword, checkedPositions)
            }
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

                    // Isi daftar utama untuk RecyclerView
                    fullList.clear()
                    fullList.addAll(data)

                    // Tampilkan semua data awal
                    adapter.filterList(fullList)

                    // Update jumlah total karyawan
                    totalkaryawanText.text = "${fullList.size} Orang"

                    // Setup dropdown dengan data dari API
                    setupPosisiDropdown(data)

                } else {
                    Log.d("API_DATA", "Error body: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Todo>, t: Throwable) {
                Log.d("DEBUG_FLOW", "API FAILED: ${t.message}")
            }
        })
    }

    private fun setupPosisiDropdown(data: List<MemberRequest>) {
        // daftar posisi
        val daftarNamaPosisi = data.mapNotNull { it.role }.distinct()

        // daftar String menjadi daftar PosisiItem
        val daftarPosisiItem = daftarNamaPosisi.map { namaPosisi ->
            PosisiItem(nama = namaPosisi, isChecked = false)
        }.toMutableList()

        // opsi "Semua Posisi" default
        daftarPosisiItem.add(0, PosisiItem(nama = "Semua Posisi", isChecked = true))

        val dropdownAdapter = PosisiAdapter(
            requireContext(),
            R.layout.dropdown_posisi_karyawan,
            daftarPosisiItem
        )
        dropdownPosisi.setAdapter(dropdownAdapter)

        // logika untuk (MULTI-CHOICE)
        dropdownPosisi.setOnItemClickListener { parent, _, position, _ ->
            val adapter = parent.adapter as PosisiAdapter
            val clickedItem = adapter.getItem(position)
            val allItems = adapter.items

            //  Balik status centang item yang diklik
            clickedItem.isChecked = !clickedItem.isChecked

            // Logika khusus "Semua Posisi"
            val semuaPosisiItem = allItems.firstOrNull { it.nama == "Semua Posisi" }
            if (clickedItem.nama == "Semua Posisi" && clickedItem.isChecked) {
                allItems.forEach { if (it.nama != "Semua Posisi") it.isChecked = false }
            } else if (clickedItem.nama != "Semua Posisi" && clickedItem.isChecked) {
                semuaPosisiItem?.isChecked = false
            }
            adapter.notifyDataSetChanged()

            val checkedItems = allItems.filter { it.isChecked }
            val checkedPositions = checkedItems.map { it.nama }

            val summaryText: String
            if (checkedPositions.contains("Semua Posisi") || checkedPositions.isEmpty()) {
                summaryText = "Semua Posisi"
                filterData(searchInput.text.toString(), listOf("Semua Posisi"))
            } else {
                summaryText = checkedPositions.take(2).joinToString(", ") +
                        if (checkedPositions.size > 2) ", ..." else ""
                filterData(searchInput.text.toString(), checkedPositions)
            }

            dropdownPosisi.setText(summaryText, false)
        }

    }

    private fun filterData(namaKeyword: String, posisiKeywords: List<String>) {
        var hasilFilter = fullList.toList()

        // Filter berdasarkan NAMA
        if (namaKeyword.isNotBlank()) {
            hasilFilter = hasilFilter.filter { member ->
                val nameMatch = member.name.contains(namaKeyword, ignoreCase = true)
                val usernameMatch = member.display_name.contains(namaKeyword, ignoreCase = true)
                nameMatch || usernameMatch
            }
        }

        // Filter Posisi (MULTI-CHOICE)
        val isFilteringByPosition = !posisiKeywords.contains("Semua Posisi") && posisiKeywords.isNotEmpty()

        if (isFilteringByPosition) {
            hasilFilter = hasilFilter.filter { member ->
                posisiKeywords.any { keyword -> member.role?.equals(keyword, ignoreCase = true) == true }
            }
        }
        adapter.filterList(hasilFilter)
    }

    fun showBottomSheetDetail(item: MemberRequest) {
        val bottomSheet = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.popup_detail_karyawan, null)

        // val img = view.findViewById<ImageView>(R.id.profileDetail) // Uncomment jika perlu
        val nama = view.findViewById<TextView>(R.id.namaDetail)
        val email = view.findViewById<TextView>(R.id.email_detail)

        nama.text = item.name
        email.text = item.display_name

        bottomSheet.setContentView(view)
        bottomSheet.behavior.peekHeight = 900
        bottomSheet.show()
    }
}
