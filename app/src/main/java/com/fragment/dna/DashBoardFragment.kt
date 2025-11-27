package com.fragment.dna

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.MemberAPI.MemberRequest
import com.MemberAPI.RetrofitClient
import com.MemberAPI.Todo
import com.example.dna.AdapterClass
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




    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("DEBUG_FLOW ","Fragment created")


        val view = inflater.inflate(R.layout.fragment_dash_board, container, false)
        val searchInput: EditText = view.findViewById(R.id.searchInput)
        searchInput.addTextChangedListener { text ->
            val keyword = text.toString()
            val filtered = fullList  .filter {
                it.name.contains(keyword, ignoreCase = true) ||
                        it.username.contains(keyword, ignoreCase = true)
            }
            adapter.filterList(filtered)
        }


        recycleView = view.findViewById(R.id.recycleViews)
        recycleView.layoutManager = LinearLayoutManager(requireContext())


        adapter = AdapterClass(dataList) { item ->
            showBottomSheetDetail(item)
        }

        recycleView.adapter = adapter
        Log.d("DEBUG_FLOW","Calling API......")

        // Ambil API
        RetrofitClient.instance.getTodo().enqueue(object : Callback<Todo> {

            override fun onResponse(call: Call<Todo>, response: Response<Todo>) {
                Log.d("DEBUG_FLOW", "API onResponse triggered")

                if (response.isSuccessful) {
                    val data = response.body()?.users ?:
                    emptyList()

                    Log.d("API_DATA","Data: $data")

                    fullList.clear()
                    fullList.addAll(data)
                    dataList.clear()
                    dataList.addAll(data)
                    adapter.notifyDataSetChanged()
                    var totalkaryawanText: TextView = view.findViewById(R.id.jmlhkaryawan)
                    val totalKaryawan = dataList.size
                    totalkaryawanText.text = "$totalKaryawan"
                } else {
                    Log.d("API_DATA","Error body: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Todo>, t: Throwable) {
                Log.d("DEBUG_FLOW","API FAILED: ${t.message}")
            }
        })





        return view
    }

    fun showBottomSheetDetail(item: MemberRequest) {
        val bottomSheet = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.popup_detail_karyawan, null)

        val img = view.findViewById<ImageView>(R.id.profileDetail)
        val nama = view.findViewById<TextView>(R.id.namaDetail)
        val email = view.findViewById<TextView>(R.id.email_detail)


        nama.text = item.name
        email.text = item.username

        bottomSheet.setContentView(view)
        bottomSheet.behavior.peekHeight = 900
        bottomSheet.show()
    }
}